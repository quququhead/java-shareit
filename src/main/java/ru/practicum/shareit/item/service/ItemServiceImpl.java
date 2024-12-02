package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<ItemDto> findAllItemsOfUser(long userId) {
        log.debug("findAllItemsOfUser {}", userId);
        User owner = receiveUser(userId);
        return itemStorage.getAllItemsOfOwner(owner)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findItem(long itemId) {
        log.debug("findItem {}", itemId);
        return ItemMapper.mapToItemDto(receiveItem(itemId));
    }

    @Override
    public Collection<ItemDto> findItemByText(String text) {
        log.debug("findItemByText {}", text);
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
        log.debug("{} createItem {}", userId, itemRequest);
        User user = receiveUser(userId);
        Item item = ItemMapper.mapToItem(user, itemRequest);
        return ItemMapper.mapToItemDto(itemStorage.addItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, UpdateItemRequest itemRequest) {
        log.debug("{} updateItem {}, {}", userId, itemId, itemRequest);
        User user = receiveUser(userId);
        Item item = receiveItem(itemId);
        if (!item.getOwner().equals(user)) {
            throw new NoSuchElementException("You do not have such permission!");
        }
        return ItemMapper.mapToItemDto(ItemMapper.updateItemFields(item, itemRequest));
    }

    private Item receiveItem(long itemId) {
        return itemStorage.getItem(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item " + itemId + " was not found!"));
    }

    private User receiveUser(long userId) {
        return userStorage.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
    }
}
