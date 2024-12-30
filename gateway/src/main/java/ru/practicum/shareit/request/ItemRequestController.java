package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> findAllItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("FindAllItemRequestsOfUser userId={}", userId);
        return itemRequestClient.findAllItemRequestsOfUser(userId);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Object> findAllItemRequestsExceptUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("FindAllItemRequestsExceptUser userId={}", userId);
        return itemRequestClient.findAllItemRequestsExceptUser(userId);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<Object> findItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable long requestId) {
        log.info("FindItemRequest requestId={}, userId={}", requestId, userId);
        return itemRequestClient.findItemRequest(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @Valid @RequestBody NewItemRequestRequest itemRequestRequest) {
        log.info("CreateItemRequest {}, userId={}", itemRequestRequest, userId);
        return itemRequestClient.createItemRequest(userId, itemRequestRequest);
    }
}
