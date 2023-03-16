package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
@Slf4j
public class ValidateService {

    public void validateUser(User user){
        if (user.getName() == null||user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void validateFilm(Film film) throws ValidationException{
        if(film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Фильм не прошёл валидацию: {}", film);
            throw new ValidationException("Ошибка валидации фильма");
        }
    }
}
