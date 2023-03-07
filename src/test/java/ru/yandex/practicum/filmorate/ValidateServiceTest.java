package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;

public class ValidateServiceTest {
    ValidateService validateService = new ValidateService();

    public void catchValidationExceptionForUser(User user) {
        try {
            validateService.validateUser(user);
            Assertions.assertEquals(user.getName(), user.getLogin());
        } catch (ValidationException e) {
            Assertions.assertEquals(e.getMessage(), "Ошибка валидации пользователя");
        }
    }

    public void catchValidationExceptionForFilm(Film film) {
        try {
            validateService.validateFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals(e.getMessage(), "Ошибка валидации фильма");
        }
    }

    @Test
    public void shouldValidateUser() {
        User userWithEmptyEmail = User.builder()
                .id(1)
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 1, 1))
                .email("")
                .build();
        catchValidationExceptionForUser(userWithEmptyEmail);

        User userWithEmptyLogin = User.builder()
                .id(1)
                .login("")
                .name("name")
                .birthday(LocalDate.of(1990, 1, 1))
                .email("email@mail.ru")
                .build();
        catchValidationExceptionForUser(userWithEmptyLogin);

        User userWithEmptyName = User.builder()
                .id(1)
                .login("login")
                .name("")
                .birthday(LocalDate.of(1990, 1, 1))
                .email("email@mail.ru")
                .build();
        catchValidationExceptionForUser(userWithEmptyName);

        User userFromFuture = User.builder()
                .id(1)
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2030, 1, 1))
                .email("email@mail.ru")
                .build();
        catchValidationExceptionForUser(userFromFuture);
    }

    @Test
    public void shouldValidateFilm() {
        Film filmWithEmptyName = Film.builder()
                .id(1)
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(100)
                .build();
        catchValidationExceptionForFilm(filmWithEmptyName);

        Film filmWithVeryBigDescription = Film.builder()
                .id(1)
                .name("name")
                .description(new String(new char[201]))
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(100)
                .build();
        catchValidationExceptionForFilm(filmWithVeryBigDescription);

        Film filmWithOldReleaseDate = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1894, 1, 1))
                .duration(100)
                .build();
        catchValidationExceptionForFilm(filmWithOldReleaseDate);

        Film filmWithNegativeDuration = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(-1)
                .build();
        catchValidationExceptionForFilm(filmWithNegativeDuration);
    }
}
