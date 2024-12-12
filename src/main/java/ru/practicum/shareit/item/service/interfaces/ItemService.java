package ru.practicum.shareit.item.service.interfaces;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findAllItemsOfUser(long userId);

    ItemDto findItem(long itemId);

    Collection<ItemDto> findItemByText(String text);

    ItemDto createItem(long userId, NewItemRequest itemRequest);

    ItemDto updateItem(long userId, long itemId, UpdateItemRequest itemRequest);
}
