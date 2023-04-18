package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface MainStorage<T> {
    T addObject(T object);

    void updateObject(T object);

    T getObjectById(int id);

    List<T> getObjects();
}
