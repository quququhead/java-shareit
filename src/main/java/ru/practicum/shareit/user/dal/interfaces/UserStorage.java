package ru.practicum.shareit.user.dal.interfaces;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserStorage {
    Optional<User> getUser(long id);

    User addUser(User user);

    void removeUser(long id);

    boolean isEmailNotUnique(User user);
}
