package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Captor
    private ArgumentCaptor<ItemRequest> argumentCaptor;

    @Test
    void findAllItemRequestsOfUser() {
        long userId = 0;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        ru.practicum.shareit.request.model.ItemRequest itemRequest = new ru.practicum.shareit.request.model.ItemRequest();
        itemRequest.setId(0L);
        itemRequest.setDescription("Description");
        itemRequest.setAuthor(user);
        itemRequest.setCreated(null);
        itemRequest.setItems(List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByAuthorIdOrderByCreatedDesc(userId)).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestDtos = itemRequestService.findAllItemRequestsOfUser(userId);

        assertNotNull(itemRequestDtos);
        assertEquals(1, itemRequestDtos.size());
        verify(itemRequestRepository).findByAuthorIdOrderByCreatedDesc(userId);

    }

    @Test
    void findAllItemRequestsExceptUser() {
        long userId = 0;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        User oneMoreUser = new User();
        user.setId(1L);
        user.setName("quhead");
        user.setEmail("shelbyboss@yandex.ru");

        ru.practicum.shareit.request.model.ItemRequest itemRequest = new ru.practicum.shareit.request.model.ItemRequest();
        itemRequest.setId(0L);
        itemRequest.setDescription("Description");
        itemRequest.setAuthor(oneMoreUser);
        itemRequest.setCreated(null);
        itemRequest.setItems(List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByAuthorIdNotOrderByCreatedDesc(userId)).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestDtos = itemRequestService.findAllItemRequestsExceptUser(userId);

        assertNotNull(itemRequestDtos);
        assertEquals(1, itemRequestDtos.size());
        verify(itemRequestRepository).findByAuthorIdNotOrderByCreatedDesc(userId);
    }

    @Test
    void findItemRequest() {
        long userId = 0L;
        long requestId = 0L;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        ru.practicum.shareit.request.model.ItemRequest itemRequest = new ru.practicum.shareit.request.model.ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setDescription("Description");
        itemRequest.setAuthor(user);
        itemRequest.setCreated(null);
        itemRequest.setItems(List.of());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));

        ItemRequestDto itemRequestDto = itemRequestService.findItemRequest(userId, requestId);

        assertEquals(itemRequest.getId(), itemRequestDto.getId());
    }

    @Test
    void createItemRequest() {
        long userId = 0L;

        User user = new User();
        user.setId(userId);
        user.setName("Gleb");
        user.setEmail("bossshelby@yandex.ru");

        ItemRequestRequest itemRequestRequest = new ItemRequestRequest();
        itemRequestRequest.setDescription("Description");

        ru.practicum.shareit.request.model.ItemRequest itemRequest = ItemRequestMapper.toItemRequest(user, itemRequestRequest);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(userId, itemRequestRequest);

        assertEquals(itemRequestRequest.getDescription(), itemRequestDto.getDescription());
        verify(itemRequestRepository).save(itemRequest);
    }
}