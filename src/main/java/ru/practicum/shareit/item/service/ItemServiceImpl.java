package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Collection<ItemDto> findAllItemsOfUser(long userId) {
        log.debug("findAllItemsOfUser {}", userId);
        receiveUser(userId);
        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemWithDatesDto findItem(long itemId) {
        log.debug("findItem {}", itemId);
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartDesc(itemId, BookingStatus.APPROVED, now);
        Booking nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId, BookingStatus.APPROVED, now);
        return ItemMapper.mapToItemWithDatesDto(receiveItem(itemId), lastBooking, nextBooking);
    }

    @Override
    public Collection<ItemDto> findItemByText(String text) {
        log.debug("findItemByText {}", text);
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.findByNameIgnoreCaseContainingAndAvailableTrueOrDescriptionIgnoreCaseContainingAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(long userId, NewItemRequest itemRequest) {
        log.debug("{} createItem {}", userId, itemRequest);
        User user = receiveUser(userId);
        Item item = ItemMapper.mapToItem(user, itemRequest);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public CommentDto createComment(long userId, long itemId, NewCommentRequest commentRequest) {
        log.debug("{} createComment {}, {}", userId, itemId, commentRequest);
        User user = receiveUser(userId);
        Item item = receiveItem(itemId);
        Booking booking = bookingRepository.findByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("You do not have such permission!"));
        Comment comment = CommentMapper.mapToComment(user, item, commentRequest);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, UpdateItemRequest itemRequest) {
        log.debug("{} updateItem {}, {}", userId, itemId, itemRequest);
        User user = receiveUser(userId);
        Item item = receiveItem(itemId);
        if (item.getOwner().equals(user)) {
            return ItemMapper.mapToItemDto(ItemMapper.updateItemFields(item, itemRequest));
        }
        throw new NoSuchElementException("You do not have such permission!");
    }

    private Item receiveItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item " + itemId + " was not found!"));
    }

    private User receiveUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
    }
}
