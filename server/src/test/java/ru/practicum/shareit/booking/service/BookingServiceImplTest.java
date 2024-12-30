package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Captor
    private ArgumentCaptor<Booking> argumentCaptor;

    @Test
    void findAllBookingsOfUser() {
        long userId = 0L;
        BookingState state = BookingState.ALL;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        Item item = new Item();
        item.setId(0L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setItemRequest(null);
        item.setComments(List.of());

        Booking booking = new Booking();
        booking.setId(0L);
        booking.setStart(LocalDateTime.of(2024, 12, 30, 12, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 12, 31, 12, 0, 0));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdOrderByStart(userId)).thenReturn(List.of(booking));

        List<BookingDto> bookingDtoList = bookingService.findAllBookingsOfUser(userId, state);

        assertNotNull(bookingDtoList);
        assertEquals(1, bookingDtoList.size());
        verify(bookingRepository).findByBookerIdOrderByStart(userId);

    }

    @Test
    void findBooking() {
        long userId = 0L;
        long bookingId = 0L;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        Item item = new Item();
        item.setId(0L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setItemRequest(null);
        item.setComments(List.of());

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(LocalDateTime.of(2024, 12, 30, 12, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 12, 31, 12, 0, 0));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDto bookingDto = bookingService.findBooking(userId, bookingId);

        assertEquals(bookingId, bookingDto.getId());
    }

    @Test
    void findBookingWithException() {
        long userId = 0L;
        long bookingId = 0L;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        User oneMoreUser = new User();
        user.setId(1L);
        user.setName("quhead");
        user.setEmail("shelbyboss@yandex.ru");

        Item item = new Item();
        item.setId(0L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(oneMoreUser);
        item.setItemRequest(null);
        item.setComments(List.of());

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(LocalDateTime.of(2024, 12, 30, 12, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 12, 31, 12, 0, 0));
        booking.setItem(item);
        booking.setBooker(oneMoreUser);
        booking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(PermissionDeniedException.class, () -> bookingService.findBooking(userId, bookingId));
    }

    @Test
    void findAllBookingsForOwner() {
        long userId = 0L;
        BookingState state = BookingState.ALL;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        Item item = new Item();
        item.setId(0L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setItemRequest(null);
        item.setComments(List.of());

        Booking booking = new Booking();
        booking.setId(0L);
        booking.setStart(LocalDateTime.of(2024, 12, 30, 12, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 12, 31, 12, 0, 0));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByItem_OwnerIdOrderByStart(userId)).thenReturn(List.of(booking));

        List<BookingDto> bookingDtoList = bookingService.findAllBookingsForOwner(userId, state);

        assertNotNull(bookingDtoList);
        assertEquals(1, bookingDtoList.size());
        verify(bookingRepository).findByItem_OwnerIdOrderByStart(userId);

    }

    @Test
    void createBooking() {
        long userId = 0L;

        BookingRequest bookingDto = new BookingRequest();
        bookingDto.setItemId(0L);
        bookingDto.setStart(LocalDateTime.of(2024, 12, 30, 12, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2024, 12, 31, 12, 0, 0));

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        Item item = new Item();
        item.setId(0L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setItemRequest(null);
        item.setComments(List.of());

        Booking booking = BookingMapper.mapToBooking(user, item, bookingDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingDto result = bookingService.createBooking(userId, bookingDto);

        assertEquals(bookingDto.getItemId(), result.getItem().getId());
        assertEquals(bookingDto.getStart(), result.getStart());
        assertEquals(bookingDto.getEnd(), result.getEnd());
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateBooking() {
        long userId = 0L;
        long bookingId = 0L;
        boolean approved = true;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        Item item = new Item();
        item.setId(0L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setItemRequest(null);
        item.setComments(List.of());

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(LocalDateTime.of(2024, 12, 30, 12, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 12, 31, 12, 0, 0));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingDto result = bookingService.updateBooking(userId, bookingId, approved);

        assertEquals(booking.getStatus(), result.getStatus());
        verify(bookingRepository).save(booking);
    }
}