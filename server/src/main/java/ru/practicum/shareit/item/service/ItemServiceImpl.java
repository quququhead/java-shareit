package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
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
    private final ItemRequestRepository itemRequestRepository;

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
    public ItemWithDatesDto findItem(long userId, long itemId) {
        log.debug("{} findItem {}", userId, itemId);
        receiveUser(userId);
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartDesc(itemId, BookingStatus.APPROVED, now);
        Booking nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId, BookingStatus.APPROVED, now);
        return ItemMapper.mapToItemWithDatesDto(receiveItem(itemId), lastBooking, nextBooking);
    }

    @Override
    public Collection<ItemDto> findItemByText(long userId, String text) {
        log.debug("{} findItemByText {}", userId, text);
        receiveUser(userId);
        return itemRepository.findByNameIgnoreCaseContainingAndAvailableTrueOrDescriptionIgnoreCaseContainingAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(long userId, ItemRequest itemRequest) {
        log.debug("{} createItem {}", userId, itemRequest);
        User user = receiveUser(userId);
        Item item = ItemMapper.mapToItem(user, itemRequest);
        ru.practicum.shareit.request.model.ItemRequest requestForItem = recieveItemRequest(itemRequest.getRequestId());
        if (requestForItem != null) {
            requestForItem.addItem(item);
        }
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public CommentDto createComment(long userId, long itemId, CommentRequest commentRequest) {
        log.debug("{} createComment {}, {}", userId, itemId, commentRequest);
        User user = receiveUser(userId);
        Item item = receiveItem(itemId);
        bookingRepository.findByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new PermissionDeniedException("You do not have such permission!"));
        Comment comment = CommentMapper.mapToComment(user, item, commentRequest);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemRequest itemRequest) {
        log.debug("{} updateItem {}, {}", userId, itemId, itemRequest);
        User user = receiveUser(userId);
        Item item = receiveItem(itemId);
        if (item.getOwner().equals(user)) {
            return ItemMapper.mapToItemDto(itemRepository.save(ItemMapper.updateItemFields(item, itemRequest)));
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

    private ru.practicum.shareit.request.model.ItemRequest recieveItemRequest(Long requestId) {
        if (requestId == null) {
            return null;
        }
        return itemRequestRepository.findById(requestId).orElse(null);
    }
}
