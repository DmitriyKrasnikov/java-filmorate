package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ServiceManager;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final ServiceManager serviceManager;
    private final ValidateService validateService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(ServiceManager serviceManager, ValidateService validateService) {
        this.serviceManager = serviceManager;
        this.validateService = validateService;
        this.filmStorage = serviceManager.getFilmService().getStorage();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws ValidationException {
        validateService.validateFilm(film);
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException, FilmNotFoundException {
        validateService.validateFilm(film);
        filmStorage.updateFilm(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) throws FilmNotFoundException {
        serviceManager.getFilmService().addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws FilmNotFoundException {
        serviceManager.getUserService().isExist(userId);
        serviceManager.getFilmService().removeLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return serviceManager.getFilmService().mostPopularFilm(count);
    }
}
