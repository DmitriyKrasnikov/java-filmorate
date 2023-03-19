package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Component
public interface FilmStorage {
    int generateId();

    void addFilm(Film film);

    void updateFilm(Film film);

    Film getFilmById(int id);

    HashMap<Integer, Film> getFilms();
}
