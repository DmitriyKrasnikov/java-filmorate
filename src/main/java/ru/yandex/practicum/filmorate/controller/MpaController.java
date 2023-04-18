package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ServiceManager;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final FilmService filmService;

    @Autowired
    public MpaController(ServiceManager serviceManager) {
        this.filmService = serviceManager.getFilmService();
    }

    @GetMapping
    public List<Rating> getRatings() {
        log.info("Получение видов рейтинга");
        return filmService.getRatings();
    }

    @GetMapping("{id}")
    public Rating getRatingById(@PathVariable Integer id) {
        log.info("Получение рейтинга с id {}", id);
        return filmService.getRatingById(id);
    }
}
