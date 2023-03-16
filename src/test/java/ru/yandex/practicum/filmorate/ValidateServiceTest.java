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

    @Test
    public void shouldValidateUser() {
        User userWithEmptyName = User.builder()
                .id(1)
                .login("login")
                .name("")
                .birthday(LocalDate.of(1990, 1, 1))
                .email("email@mail.ru")
                .build();
        validateService.validateUser(userWithEmptyName);
        Assertions.assertEquals(userWithEmptyName.getName(), userWithEmptyName.getLogin());
    }

    @Test
    public void shouldValidateFilm() {
        Film filmWithOldReleaseDate = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1894, 1, 1))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> validateService.validateFilm(filmWithOldReleaseDate));
    }
}
