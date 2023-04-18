package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public interface FilmStorage extends MainStorage<Film> {
    int generateId();
}
