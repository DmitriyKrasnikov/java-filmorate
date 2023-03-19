package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
public class FilmService {

    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public void addFilm(Film film) throws ValidationException {
        if (film == null) {
            throw new ValidationException("Передан пустой объект");
        }
        if (storage.getFilms().containsValue(film)) {
            log.debug("Фильм уже существует {}", film);
            throw new ValidationException("Ошибка при добавлении фильма");
        }
        storage.addFilm(film);
    }

    public void updateFilm(Film film) throws ValidationException, FilmNotFoundException {
        if (film == null) {
            throw new ValidationException("Передан пустой объект");
        }
        if (!storage.getFilms().containsKey(film.getId())) {
            log.debug("Список фильмов пуст или фильм не существует {}", film);
            throw new FilmNotFoundException("Ошибка при обновлении фильма");
        }
        storage.updateFilm(film);
    }

    public Film getFilmById(int id) throws FilmNotFoundException {
        if (!storage.getFilms().containsKey(id)) {
            log.debug("Список фильмов пуст или фильм не существует {}", id);
            throw new FilmNotFoundException("Ошибка при обновлении фильма");
        }
        return storage.getFilmById(id);
    }

    public HashMap<Integer, Film> getFilms() {
        return storage.getFilms();
    }

    public void addLike(int userId, int filmId) throws FilmNotFoundException {
        isExist(filmId);
        storage.getFilms().get(filmId).getLikes().add(userId);
    }

    public void removeLike(int userId, int filmId) throws FilmNotFoundException {
        isExist(filmId);
        storage.getFilms().get(filmId).getLikes().remove(userId);
    }

    public List<Film> mostPopularFilm(int count) {
        return new ArrayList<>(storage.getFilms().values()).stream()
                .sorted((f0, f1) -> {
                    int comp = f0.getLikes().size() - f1.getLikes().size();
                    return comp * -1;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    public void isExist(int filmId) throws FilmNotFoundException {
        if (!storage.getFilms().containsKey(filmId)) {
            log.debug("Попытка взаимодействовать с несуществующим фильмом {}", filmId);
            throw new FilmNotFoundException("Фильма с id " + filmId + " не существует");
        }
    }
}
