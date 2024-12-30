package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("FindAllItemsOfUser {}", userId);
        return itemClient.findAllItemsOfUser(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable Long itemId) {
        log.info("FindItem {}", itemId);
        return itemClient.findItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam @NonNull String text) {
        log.info("FindItemByText {}", text);
        return itemClient.findItemByText(userId, text);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody NewItemRequest itemRequest) {
        log.info("CreateItem {}", itemRequest);
        return itemClient.createItem(userId, itemRequest);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long itemId,
                                                @Valid @RequestBody NewCommentRequest commentRequest) {
        log.info("CreateComment {}", commentRequest);
        return itemClient.createComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody UpdateItemRequest itemRequest) {
        log.info("UpdateItem {}", itemRequest);
        return itemClient.updateItem(userId, itemId, itemRequest);
    }
}
