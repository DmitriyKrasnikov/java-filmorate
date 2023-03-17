package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Component
@Slf4j
@Getter
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public int generateId() {
        return ++filmId;
    }

    @Override
    public void addFilm(Film film) throws ValidationException {
        if (films.containsValue(film)) {
            log.debug("Фильм уже существует {}", film);
            throw new ValidationException("Ошибка при добавлении фильма");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
    }

    @Override
    public void updateFilm(Film film) throws FilmNotFoundException {
        if (!films.containsKey(film.getId())) {
            log.debug("Список фильмов пуст или фильм не существует {}", film);
            throw new FilmNotFoundException("Ошибка при обновлении фильма");
        } else {
            log.info("Обновлен фильм: {}", film);
            films.replace(film.getId(), film);
        }
    }

    @Override
    public Film getFilmById(int id) throws FilmNotFoundException {
        if (!films.containsKey(id)) {
            log.debug("Список фильмов пуст или фильм не существует {}", id);
            throw new FilmNotFoundException("Ошибка при обновлении фильма");
        }
        return films.get(id);
    }

    @Override
    public HashMap<Integer,Film> getFilms(){return films;}
}
