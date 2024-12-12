package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.interfaces.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> findAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAllItemsOfUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItem(@PathVariable long itemId) {
        return itemService.findItem(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemByText(@RequestParam String text) {
        return itemService.findItemByText(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody NewItemRequest itemRequest) {
        return itemService.createItem(userId, itemRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId,
                              @RequestBody UpdateItemRequest itemRequest) {
        return itemService.updateItem(userId, itemId, itemRequest);
    }
}
