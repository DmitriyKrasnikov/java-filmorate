package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ServiceManager;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final ValidateService validateService;
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(ServiceManager serviceManager, ValidateService validateService) {
        this.validateService = validateService;
        this.filmService = serviceManager.getFilmService();
        this.userService = serviceManager.getUserService();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        log.info("Получение фильма с id {}", filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(filmService.getFilms().values());
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws ValidationException {
        validateService.validateFilm(film);
        filmService.addFilm(film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException, FilmNotFoundException {
        validateService.validateFilm(film);
        filmService.updateFilm(film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) throws FilmNotFoundException {
        filmService.addLike(userId, id);
        log.info("Пользователь {} поставил лайк фильму {}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws FilmNotFoundException {
        userService.isExist(userId);
        filmService.removeLike(userId, id);
        log.info("Пользователь {} убрал лайк у фильма {}", userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Получение {} самых популярных фильмов", count);
        return filmService.mostPopularFilm(count);
    }
}
