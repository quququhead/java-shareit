package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> argumentCaptor;

    @Test
    void findUser_whenUserValid_thenReturnedUser() {
        long userId = 0L;

        User expectedUser = new User();
        expectedUser.setId(0L);
        expectedUser.setName("Gleb");
        expectedUser.setEmail("bossshelby@yandex.ru");

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(0L);
        expectedUserDto.setName("Gleb");
        expectedUserDto.setEmail("bossshelby@yandex.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserDto actualUserDto = userService.findUser(expectedUserDto.getId());

        assertEquals(expectedUserDto.getId(), actualUserDto.getId());
        assertEquals(expectedUserDto.getName(), actualUserDto.getName());
        assertEquals(expectedUserDto.getEmail(), actualUserDto.getEmail());
    }

    @Test
    void findUser_whenUserNotFound_thenNoSuchElementExceptionThrown() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findUser(userId));
    }


    @Test
    void createUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Gleb");
        userRequest.setEmail("bossshelby@yandex.ru");

        User expectedUser = UserMapper.mapToUser(userRequest);

        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto actualUserDto = userService.createUser(userRequest);

        assertEquals(userRequest.getName(), actualUserDto.getName());
        assertEquals(userRequest.getEmail(), actualUserDto.getEmail());
        verify(userRepository).save(expectedUser);
    }

    @Test
    void updateUser() {
        long userId = 0L;
        UserRequest newUserRequest = new UserRequest();
        newUserRequest.setName("quhead");
        newUserRequest.setEmail("shelbyboss@yandex.ru");

        User oldUser = new User();
        oldUser.setId(0L);
        oldUser.setName("Gleb");
        oldUser.setEmail("bossshelby@yandex.ru");

        User newUser = new User();
        newUser.setId(0L);
        newUser.setName("quhead");
        newUser.setEmail("shelbyboss@yandex.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(newUser)).thenReturn(newUser);

        userService.updateUser(userId, newUserRequest);

        verify(userRepository).save(argumentCaptor.capture());
        User savedUser = argumentCaptor.getValue();

        assertEquals(oldUser.getId(), savedUser.getId());
        assertEquals(newUser.getName(), savedUser.getName());
        assertEquals(newUser.getEmail(), savedUser.getEmail());

    }

    @Test
    void deleteUser() {
        long userId = 0L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
}