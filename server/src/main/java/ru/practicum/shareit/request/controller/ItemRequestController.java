package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.service.interfaces.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestDto> findAllItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.findAllItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllItemRequestsExceptUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.findAllItemRequestsExceptUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long requestId) {
        return itemRequestService.findItemRequest(userId, requestId);
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody ItemRequestRequest itemRequestRequest) {
        return itemRequestService.createItemRequest(userId, itemRequestRequest);
    }
}
