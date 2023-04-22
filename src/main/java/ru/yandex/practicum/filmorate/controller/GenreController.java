package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ServiceManager;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final FilmService filmService;

    @Autowired
    public GenreController(ServiceManager serviceManager) {
        this.filmService = serviceManager.getFilmService();
    }

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получение всех жанров");
        return filmService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        log.info("Получение жанра с id {}", id);
        return filmService.getGenreById(id);
    }
}
