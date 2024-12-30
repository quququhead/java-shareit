package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.interfaces.ItemRequestService;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemRequestDto> findAllItemRequestsOfUser(long userId) {
        log.debug("findAllItemRequestsOfUser {}", userId);
        receiveUser(userId);
        return itemRequestRepository.findByAuthorIdOrderByCreatedDesc(userId).stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> findAllItemRequestsExceptUser(long userId) {
        log.debug("findAllItemRequestsExceptUser {}", userId);
        receiveUser(userId);
        return itemRequestRepository.findByAuthorIdNotOrderByCreatedDesc(userId).stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto findItemRequest(long userId, long requestId) {
        log.debug("{} findItemRequest {}", userId, requestId);
        receiveUser(userId);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException("Request " + requestId + " was not found!")));
    }

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestRequest itemRequestRequest) {
        log.debug("{} createItemRequest {}", userId, itemRequestRequest);
        User user = receiveUser(userId);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(ItemRequestMapper.toItemRequest(user, itemRequestRequest)));
    }

    private User receiveUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
    }
}
