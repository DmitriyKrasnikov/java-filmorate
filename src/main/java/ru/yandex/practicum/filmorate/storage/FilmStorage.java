package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Component
public interface FilmStorage {
    int generateId();

    void addFilm(Film film) throws ValidationException;

    void updateFilm(Film film) throws ValidationException, FilmNotFoundException;

    Film getFilmById(int id) throws FilmNotFoundException;

    HashMap<Integer, Film> getFilms();
}
