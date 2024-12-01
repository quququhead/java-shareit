package ru.practicum.shareit.item.dal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dal.interfaces.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemmoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> getAllItemsOfOwner(User owner) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(owner))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItem(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> getAllItemsByText(String text) {
        return items.values()
                .stream()
                .filter(item -> (StringUtils.containsIgnoreCase(item.getName(), text)
                        || StringUtils.containsIgnoreCase(item.getDescription(), text)) && item.isAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
