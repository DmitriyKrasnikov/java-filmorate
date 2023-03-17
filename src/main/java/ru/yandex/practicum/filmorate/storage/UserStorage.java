package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Component
public interface UserStorage {
    int generateId();

    void addUser(User user) throws ValidationException;

    void updateUser(User user) throws ValidationException, UserNotFoundException;

    User getUserById(int id) throws UserNotFoundException;

    HashMap<Integer, User> getUsers();
}
