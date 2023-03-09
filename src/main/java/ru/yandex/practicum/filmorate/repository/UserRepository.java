package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
@Component
@Getter
@Slf4j
public class UserRepository {
    HashMap<Integer, User> users = new HashMap<>();
    int generalId = 0;

    private int generateId() {
        return ++generalId;
    }

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

    public void updateUser(User user) throws ValidationException {
        if(users.isEmpty() || !users.containsKey(user.getId())){
            log.debug("Список пуст, либо пользователь: {} не существует", user);
            throw new ValidationException("Ошибка при обновлении пользователя");
        } else {
            users.replace(user.getId(), user);
            log.info("Пользователь обновлен: {}", user);
        }
    }
}
