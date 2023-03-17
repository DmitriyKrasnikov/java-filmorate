package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
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
