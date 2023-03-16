package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Component
@Slf4j
@Getter
public class FilmRepository {
    public HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    public int generateId() {
         return ++filmId;
     }

    public void addFilm(Film film) throws ValidationException{
        if (films.containsValue(film)){
            log.debug("Фильм уже существует {}", film);
            throw new ValidationException("Ошибка при добавлении фильма");
        }
        film.setId(generateId());
        films.put(film.getId(),film);
        log.info("Добавлен фильм {}", film);
    }

    public void updateFilm(Film film) throws ValidationException{
        if (!films.containsKey(film.getId()) || films.isEmpty()){
        log.debug("Список фильмов пуст или фильм не существует {}", film);
        throw new ValidationException("Ошибка при обновлении фильма");
        }else {
            log.info("Обновлен фильм: {}", film);
            films.replace(film.getId(),film);
        }
    }
}
