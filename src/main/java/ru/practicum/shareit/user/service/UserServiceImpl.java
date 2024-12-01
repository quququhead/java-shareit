package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dal.interfaces.UserStorage;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto findUser(long userId) {
        return UserMapper.mapToUserDto(userStorage.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!")));
    }

    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        User user = UserMapper.mapToUser(userRequest);
        if (userStorage.isEmailNotUnique(user)) {
            throw new IllegalArgumentException("Email (" + user.getEmail() + ") is already in use!");
        }
        return UserMapper.mapToUserDto(userStorage.addUser(user));
    }

    @Override
    public UserDto updateUser(long userId, UpdateUserRequest userRequest) {
        if (userStorage.isEmailNotUnique(UserMapper.mapToUser(userRequest))) {
            throw new IllegalArgumentException("Email (" + userRequest.getEmail() + ") is already in use!");
        }
        User user = userStorage.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
        return UserMapper.mapToUserDto(UserMapper.updateUserFields(user, userRequest));
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.removeUser(userId);
    }
}
