package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Getter
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int generalId = 0;

    public int generateId() {
        return ++generalId;
    }

    @Override
    public User addObject(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void updateObject(User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public User getObjectById(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getObjects() {
        return new ArrayList<>(users.values());
    }
}
