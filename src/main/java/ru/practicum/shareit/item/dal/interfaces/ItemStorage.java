package ru.practicum.shareit.item.dal.interfaces;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Collection<Item> getAllItemsOfOwner(User owner);

    Optional<Item> getItem(long id);

    Collection<Item> getAllItemsByText(String text);

    Item addItem(Item item);
}
