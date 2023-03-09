package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final ValidateService validateService = new ValidateService();
    private final FilmRepository filmRepository = new FilmRepository();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(filmRepository.getFilms().values());
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws ValidationException {
        validateService.validateFilm(film);
        filmRepository.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
        validateService.validateFilm(film);
        filmRepository.updateFilm(film);
        return film;
    }


}
