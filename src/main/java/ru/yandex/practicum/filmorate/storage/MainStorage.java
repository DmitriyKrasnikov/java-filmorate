package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MainStorage<T> {
    T addObject(T object);

    void updateObject(T object);

    T getObjectById(int id);

    List<T> getObjects();
}
