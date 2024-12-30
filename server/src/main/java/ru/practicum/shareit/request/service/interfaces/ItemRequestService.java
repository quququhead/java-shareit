package ru.practicum.shareit.request.service.interfaces;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequest;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> findAllItemRequestsOfUser(long userId);

    List<ItemRequestDto> findAllItemRequestsExceptUser(long userId);

    ItemRequestDto findItemRequest(long userId, long requestId);

    ItemRequestDto createItemRequest(long userId, ItemRequestRequest itemRequestRequest);
}
