package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dal.interfaces.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.user.dal.interfaces.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<ItemDto> findAllItemsOfUser(long userId) {
        User owner = userStorage.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
        return itemStorage.getAllItemsOfOwner(owner)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findItem(long itemId) {
        return ItemMapper.mapToItemDto(itemStorage.getItem(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item " + itemId + " was not found!")));
    }

    @Override
    public Collection<ItemDto> findItemByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemStorage.getAllItemsByText(text)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(long userId, NewItemRequest itemRequest) {
        User user = userStorage.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
        Item item = ItemMapper.mapToItem(user, itemRequest);
        return ItemMapper.mapToItemDto(itemStorage.addItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, UpdateItemRequest itemRequest) {
        User user = userStorage.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
        Item item = itemStorage.getItem(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item " + itemId + " was not found!"));
        if (!item.getOwner().equals(user)) {
            throw new NoSuchElementException("You do not have such permission!");
        }
        return ItemMapper.mapToItemDto(ItemMapper.updateItemFields(item, itemRequest));
    }
}
