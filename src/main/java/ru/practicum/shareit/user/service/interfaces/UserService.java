package ru.practicum.shareit.user.service.interfaces;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto findUser(long userId);

    UserDto createUser(NewUserRequest userRequest);

    UserDto updateUser(long userId, UpdateUserRequest userRequest);

    void deleteUser(long userId);
}
