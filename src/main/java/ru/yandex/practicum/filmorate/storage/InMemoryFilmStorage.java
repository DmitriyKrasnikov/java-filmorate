package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public Film addObject(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void updateObject(Film film) {
        films.replace(film.getId(), film);
    }

    @Override
    public Film getObjectById(int id) {
        return films.get(id);
    }

    @Override
    public List<Film> getObjects() {
        return new ArrayList<>(films.values());
    }
}
