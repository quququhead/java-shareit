package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> findAllBookingsOfUser(long userId, BookingState state) {
        log.debug("{} findAllBookingsOfUser {}", userId, state);
        receiveUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList;
        if (state.equals(BookingState.WAITING)) {
            bookingList = bookingRepository.findByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
        } else if (state.equals(BookingState.CURRENT)) {
            bookingList = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStart(userId, now, now);
        } else if (state.equals(BookingState.REJECTED)) {
            bookingList = bookingRepository.findByBookerIdAndStatusOrBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED, userId, BookingStatus.CANCELLED);
        } else if (state.equals(BookingState.PAST)) {
            bookingList = bookingRepository.findByBookerIdAndEndBeforeOrderByStart(userId, now);
        } else if (state.equals(BookingState.FUTURE)) {
            bookingList = bookingRepository.findByBookerIdAndStartAfterOrderByStart(userId, now);
        } else {
            bookingList = bookingRepository.findByBookerIdOrderByStart(userId);
        }
        return bookingList.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto findBooking(long userId, long bookingId) {
        log.debug("{} findBooking {}", userId, bookingId);
        User user = receiveUser(userId);
        Booking booking = receiveBooking(bookingId);
        if (booking.getBooker().equals(user) || booking.getItem().getOwner().equals(user)) {
            return BookingMapper.mapToBookingDto(booking);
        }
        throw new PermissionDeniedException("You do not have such permission!");
    }

    @Override
    public List<BookingDto> findAllBookingsForOwner(long userId, BookingState state) {
        log.debug("{} findAllBookingsForOwner {}", userId, state);
        receiveUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList;
        if (state.equals(BookingState.WAITING)) {
            bookingList = bookingRepository.findByItem_OwnerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
        } else if (state.equals(BookingState.CURRENT)) {
            bookingList = bookingRepository.findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStart(userId, now, now);
        } else if (state.equals(BookingState.REJECTED)) {
            bookingList = bookingRepository.findByItem_OwnerIdAndStatusOrItem_OwnerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED, userId, BookingStatus.CANCELLED);
        } else if (state.equals(BookingState.PAST)) {
            bookingList = bookingRepository.findByItem_OwnerIdAndEndBeforeOrderByStart(userId, now);
        } else if (state.equals(BookingState.FUTURE)) {
            bookingList = bookingRepository.findByItem_OwnerIdAndStartAfterOrderByStart(userId, now);
        } else {
            bookingList = bookingRepository.findByItem_OwnerIdOrderByStart(userId);
        }
        return bookingList.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto createBooking(long userId, BookingRequest bookingDto) {
        log.debug("{} createBooking {}", userId, bookingDto);
        User user = receiveUser(userId);
        Item item = receiveItem(bookingDto.getItemId());
        return BookingMapper.mapToBookingDto(bookingRepository.save(BookingMapper.mapToBooking(user, item, bookingDto)));
    }

    @Override
    public BookingDto updateBooking(long userId, long bookingId, boolean approved) {
        log.debug("{} updateBooking {}", userId, approved);
        Booking booking = receiveBooking(bookingId);
        if (booking.getItem().getOwner().getId() == userId && booking.getStatus().equals(BookingStatus.WAITING)) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        }
        throw new PermissionDeniedException("You do not have such permission!");
    }

    private User receiveUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
    }

    private Item receiveItem(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item " + itemId + " was not found!"));
        if (!item.isAvailable()) {
            throw new PermissionDeniedException("Item " + itemId + " is not available!");
        }
        return item;
    }

    private Booking receiveBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking " + bookingId + " was not found!"));
    }
}
