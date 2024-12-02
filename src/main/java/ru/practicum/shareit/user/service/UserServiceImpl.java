package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dal.interfaces.UserStorage;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto findUser(long userId) {
        log.debug("findUser {}", userId);
        return UserMapper.mapToUserDto(receiveUser(userId));
    }

    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        log.debug("createUser {}", userRequest);
        User user = UserMapper.mapToUser(userRequest);
        if (userStorage.isEmailAlreadyExist(user)) {
            throw new IllegalArgumentException("Email (" + user.getEmail() + ") is already in use!");
        }
        return UserMapper.mapToUserDto(userStorage.addUser(user));
    }

    @Override
    public UserDto updateUser(long userId, UpdateUserRequest userRequest) {
        log.debug("{} updateUser {}", userId, userRequest);
        User user = receiveUser(userId);
        if (userStorage.isEmailAlreadyExist(UserMapper.mapToUser(userRequest))) {
            if (!user.getEmail().equals(userRequest.getEmail())) {
                throw new IllegalArgumentException("Email (" + userRequest.getEmail() + ") is already in use!");
            }
        }
        return UserMapper.mapToUserDto(UserMapper.updateUserFields(user, userRequest));
    }

    @Override
    public void deleteUser(long userId) {
        log.debug("deleteUser {}", userId);
        userStorage.removeUser(userId);
    }

    private User receiveUser(long userId) {
        return userStorage.getUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
    }
}
