package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findAllItemsOfUser(long userId);

    ItemWithDatesDto findItem(long userId, long itemId);

    Collection<ItemDto> findItemByText(long userId, String text);

    ItemDto createItem(long userId, ItemRequest itemRequest);

    CommentDto createComment(long userId, long itemId, CommentRequest commentRequest);

    ItemDto updateItem(long userId, long itemId, ItemRequest itemRequest);
}
