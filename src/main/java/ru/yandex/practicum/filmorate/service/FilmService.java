package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDaoStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
public class FilmService {
    private final FilmDaoStorage storage;

    @Autowired
    public FilmService(FilmDaoStorage storage) {
        this.storage = storage;
    }

    public void addFilm(Film film) {
        storage.addObject(film);
    }

    public void updateFilm(Film film) {
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        storage.updateObject(film);
    }

    public Film getFilmById(int id) throws FilmNotFoundException {
        return storage.getObjectById(id);
    }

    public List<Film> getFilms() {
        return storage.getObjects();
    }

    public void addLike(int userId, int filmId) {
        storage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) throws FilmNotFoundException {
        storage.removeLike(userId, filmId);
    }

    public List<Film> mostPopularFilm(int count) {
        return storage.mostPopularFilm(count);
    }

    public List<Genre> getGenres() {
        return storage.getGenres();
    }

    public Genre getGenreById(int id) {
        return storage.getGenreById(id);
    }

    public List<Rating> getRatings() {
        return storage.getRatings();
    }

    public Rating getRatingById(int id) {
        return storage.getRatingById(id);
    }
}
