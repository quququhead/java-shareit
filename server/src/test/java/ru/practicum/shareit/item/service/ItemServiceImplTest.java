package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
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
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Captor
    private ArgumentCaptor<Item> argumentCaptor;

    @Test
    void findAllItemsOfUser() {
        long userId = 0L;

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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(userId)).thenReturn(List.of(item));

        Collection<ItemDto> itemDtoCollection = itemService.findAllItemsOfUser(userId);

        assertNotNull(itemDtoCollection);
        assertEquals(1, itemDtoCollection.size());
        verify(itemRepository).findByOwnerId(userId);

    }

    @Test
    void findItem() {
        long userId = 0L;
        long itemId = 0L;

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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartDesc(eq(itemId), eq(BookingStatus.APPROVED), any())).thenReturn(null);
        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(eq(itemId), eq(BookingStatus.APPROVED), any())).thenReturn(null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemWithDatesDto itemWithDatesDto = itemService.findItem(userId, itemId);

        assertEquals(item.getId(), itemWithDatesDto.getId());
        verify(itemRepository).findById(itemId);
    }

    @Test
    void findItemByText() {
        long userId = 0L;
        String text = "nameAndDescription";

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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findByNameIgnoreCaseContainingAndAvailableTrueOrDescriptionIgnoreCaseContainingAndAvailableTrue(text, text)).thenReturn(List.of(item));

        Collection<ItemDto> itemDtoCollection = itemService.findItemByText(userId, text);

        assertNotNull(itemDtoCollection);
        assertEquals(1, itemDtoCollection.size());
        verify(itemRepository).findByNameIgnoreCaseContainingAndAvailableTrueOrDescriptionIgnoreCaseContainingAndAvailableTrue(text, text);

    }

    @Test
    void createItem() {
        long userId = 0L;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Name");
        itemRequest.setDescription("Description");
        itemRequest.setAvailable(true);
        itemRequest.setRequestId(null);

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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto itemDto = itemService.createItem(userId, itemRequest);

        assertEquals(item.getId(), itemDto.getId());
        verify(itemRepository).save(item);
    }

    @Test
    void createComment() {
        long userId = 0L;
        long itemId = 0L;

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("Text");

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
        booking.setStart(null);
        booking.setEnd(null);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(null);

        Comment comment = CommentMapper.mapToComment(user, item,commentRequest);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(userId)).thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndItemIdAndStatusAndEndBefore(eq(userId), eq(itemId), eq(BookingStatus.APPROVED), any())).thenReturn(Optional.of(booking));
        when(commentRepository.save(comment)).thenReturn(comment);

        CommentDto commentDto = itemService.createComment(userId, itemId, commentRequest);

        assertEquals(commentRequest.getText(), commentDto.getText());
        verify(commentRepository).save(comment);
    }

    @Test
    void updateItem() {
        long userId = 0L;
        long itemId = 0L;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("newName");
        itemRequest.setDescription("NewDescription");
        itemRequest.setAvailable(true);
        itemRequest.setRequestId(null);

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        Item oldItem = new Item();
        oldItem.setId(0L);
        oldItem.setName("oldName");
        oldItem.setDescription("oldDescription");
        oldItem.setAvailable(true);
        oldItem.setOwner(user);
        oldItem.setItemRequest(null);
        oldItem.setComments(List.of());

        Item newItem = ItemMapper.updateItemFields(oldItem, itemRequest);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(oldItem));
        when(itemRepository.save(newItem)).thenReturn(newItem);

        itemService.updateItem(userId, itemId, itemRequest);

        verify(itemRepository).save(argumentCaptor.capture());
        Item savedItem = argumentCaptor.getValue();

        assertEquals(oldItem.getId(), savedItem.getId());
        assertEquals(newItem.getName(), savedItem.getName());
        assertEquals(newItem.getDescription(), savedItem.getDescription());
    }
}