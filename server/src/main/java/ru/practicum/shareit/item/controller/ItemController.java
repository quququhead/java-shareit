package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
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
    public ItemWithDatesDto findItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long itemId) {
        return itemService.findItem(userId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text) {
        return itemService.findItemByText(userId, text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemRequest itemRequest) {
        return itemService.createItem(userId, itemRequest);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long itemId,
                                    @RequestBody CommentRequest commentRequest) {
        return itemService.createComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemRequest itemRequest) {
        return itemService.updateItem(userId, itemId, itemRequest);
    }
}
