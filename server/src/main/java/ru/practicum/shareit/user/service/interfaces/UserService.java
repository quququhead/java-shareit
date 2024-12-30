package ru.practicum.shareit.user.service.interfaces;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequest;

public interface UserService {
    UserDto findUser(long userId);

    UserDto createUser(UserRequest userRequest);

    UserDto updateUser(long userId, UserRequest userRequest);

    void deleteUser(long userId);
}
