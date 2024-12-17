package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dal.UserRepository;
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

    private final UserRepository userRepository;

    @Override
    public UserDto findUser(long userId) {
        log.debug("findUser {}", userId);
        return UserMapper.mapToUserDto(receiveUser(userId));
    }

    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        log.debug("createUser {}", userRequest);
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.mapToUser(userRequest)));
    }

    @Override
    public UserDto updateUser(long userId, UpdateUserRequest userRequest) {
        log.debug("{} updateUser {}", userId, userRequest);
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.updateUserFields(receiveUser(userId), userRequest)));
    }

    @Override
    public void deleteUser(long userId) {
        log.debug("deleteUser {}", userId);
        userRepository.deleteById(userId);
    }

    private User receiveUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User " + userId + " was not found!"));
    }
}
