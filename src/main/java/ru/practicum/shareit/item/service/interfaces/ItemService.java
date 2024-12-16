package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findAllItemsOfUser(long userId);

    ItemDto findItem(long itemId);

    Collection<ItemDto> findItemByText(String text);

    ItemDto createItem(long userId, NewItemRequest itemRequest);

    CommentDto createComment(long userId, long itemId, NewCommentRequest commentRequest);

    ItemDto updateItem(long userId, long itemId, UpdateItemRequest itemRequest);
}
