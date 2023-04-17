package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Component
public interface UserStorage {
    int generateId();

    void addUser(User user);

    void updateUser(User user);

    User getUserById(int id);

    HashMap<Integer, User> getUsers();
}
