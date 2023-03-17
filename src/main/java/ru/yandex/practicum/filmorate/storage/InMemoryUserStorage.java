package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
@Component
@Getter
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private final HashMap<Integer, User> users = new HashMap<>();
    private int generalId = 0;

    @Override
    public int generateId() {
        return ++generalId;
    }

    @Override
    public void addUser(User user) throws ValidationException {
        if (users.containsValue(user)) {
            log.debug("Пользователь уже существует: {}", user);
            throw new ValidationException("Пользователь уже существует");
        } else {
            user.setId(generateId());
            users.put(user.getId(), user);
            log.info("Пользователь добавлен: {}", user);
        }
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException {
        if(users.isEmpty() || !users.containsKey(user.getId())){
            log.debug("Список пуст, либо пользователь: {} не существует", user);
            throw new UserNotFoundException("Ошибка при обновлении пользователя");
        } else {
            users.replace(user.getId(), user);
            log.info("Пользователь обновлен: {}", user);
        }
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        if(users.isEmpty() || !users.containsKey(id)){
            log.debug("Список пуст, либо пользователь: {} не существует", id);
            throw new UserNotFoundException("Ошибка при обновлении пользователя");
        } else {
            return users.get(id);
        }
    }

    @Override
    public HashMap<Integer,User> getUsers(){return users;}
}
