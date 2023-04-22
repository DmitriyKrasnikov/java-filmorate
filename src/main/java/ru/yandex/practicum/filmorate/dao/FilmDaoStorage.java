package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface FilmDaoStorage extends MainStorage<Film> {

    void addLike(int userId, int filmId);

    void removeLike(int userId, int filmId);

    List<Film> mostPopularFilm(int count);

    List<Genre> getGenres();

    List<Rating> getRatings();

    Genre getGenreById(int id);

    Rating getRatingById(int id);
}
