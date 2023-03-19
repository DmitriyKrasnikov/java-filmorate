package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Component
@Getter
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int generalId = 0;

    @Override
    public int generateId() {
        return ++generalId;
    }

    @Override
    public void addUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return users;
    }
}
