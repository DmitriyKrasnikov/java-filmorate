package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDaoStorage;
import ru.yandex.practicum.filmorate.dao.UserDaoStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    public final UserDaoStorage userStorage;
    public final FilmDaoStorage filmStorage;

    @Test
    public void userDaoTest() {
        doesErrorAppearWhenAddUser();
        doesErrorAppearWhenUpdateUser();
        shouldGetCorrectUserById();
        shouldGetUsers();
        doesErrorAppearWhenAddFriend();
        doesErrorAppearWhenDeleteFriend();
        shouldGetFriends();
        shouldGetMutualFriends();
        userStorage.clearTable();
    }

    @Test
    public void filmDaoTest() {
        doesErrorAppearWhenAddFilm();
        doesErrorAppearWhenUpdateFilm();
        shouldGetCorrectFilmById();
        shouldGetFilms();
        doesErrorAppearWhenAddUser();
        doesErrorAppearWhenAddAddLike();
        doesErrorAppearWhenRemoveLike();
        shouldGetMostPopularFilm();
        shouldGetGenres();
        shouldGetCorrectGenreById();
        shouldGetRatings();
        shouldGetCorrectRatingById();
        userStorage.clearTable();
    }

    public void doesErrorAppearWhenAddUser() {
        userStorage.addObject(new User(null, "login", "name", LocalDate.of(1946, 8, 20),
                "chippinIn.@mail.ru"));
        userStorage.addObject(new User(null, "friendLog", "friendName",
                LocalDate.of(1958, 8, 20),
                "friend.@mail.ru"));
        userStorage.addObject(new User(null, "someUsLog", "someUsName",
                LocalDate.of(1976, 8, 20), "someUs.@mail.ru"));
        Assertions.assertEquals(userStorage.getObjectById(1), new User(1, "login", "name",
                LocalDate.of(1946, 8, 20), "chippinIn.@mail.ru"));
    }

    public void doesErrorAppearWhenUpdateUser() {
        userStorage.updateObject(new User(1, "updateLogin", "updateName",
                LocalDate.of(1946, 8, 20), "chippinIn.@mail.ru"));
        Assertions.assertEquals(userStorage.getObjectById(1), new User(1, "updateLogin", "updateName",
                LocalDate.of(1946, 8, 20), "chippinIn.@mail.ru"));
    }

    public void shouldGetCorrectUserById() {
        User user = userStorage.getObjectById(1);
        Assertions.assertEquals(user, new User(1, "updateLogin", "updateName",
                LocalDate.of(1946, 8, 20), "chippinIn.@mail.ru"));
    }

    public void shouldGetUsers() {
        Assertions.assertEquals(userStorage.getObjects().get(0), new User(1, "updateLogin", "updateName",
                LocalDate.of(1946, 8, 20), "chippinIn.@mail.ru"));
    }

    public void doesErrorAppearWhenAddFriend() {
        userStorage.addFriend(1, 2);
        userStorage.addFriend(1, 3);
        userStorage.addFriend(2, 1);
        userStorage.addFriend(2, 3);
    }

    public void doesErrorAppearWhenDeleteFriend() {
        userStorage.deleteFriend(1, 2);
    }

    public void shouldGetFriends() {
        Assertions.assertEquals(userStorage.getFriends(1).get(0), new User(3, "someUsLog", "someUsName",
                LocalDate.of(1976, 8, 20), "someUs.@mail.ru"));
    }

    public void shouldGetMutualFriends() {
        Assertions.assertEquals(userStorage.getMutualFriends(1, 2).get(0), new User(3, "someUsLog", "someUsName",
                LocalDate.of(1976, 8, 20), "someUs.@mail.ru"));
    }

    public void doesErrorAppearWhenAddFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        filmStorage.addObject(new Film(null, "SuperFilm", "superDescription",
                LocalDate.of(1996, 8, 20), 120, new ArrayList<>(genres),
                new Rating(1, null, null)));
        Assertions.assertEquals(filmStorage.getObjectById(1), new Film(1, "SuperFilm", "superDescription",
                LocalDate.of(1996, 8, 20), 120, new ArrayList<>(genres),
                new Rating(1, "G", "У фильма нет возрастных ограничений")));
    }

    public void doesErrorAppearWhenUpdateFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        filmStorage.updateObject(new Film(1, "boringFilm", "boringDescription",
                LocalDate.of(1996, 8, 20), 120, new ArrayList<>(genres),
                new Rating(2, null, null)));
        Assertions.assertEquals(filmStorage.getObjectById(1), new Film(1, "boringFilm",
                "boringDescription", LocalDate.of(1996, 8, 20), 120,
                new ArrayList<>(genres), new Rating(2, "PG", "детям рекомендуется смотреть фильм с родителями")));
    }

    public void shouldGetCorrectFilmById() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Film film = filmStorage.getObjectById(1);
        Assertions.assertEquals(film, new Film(1, "boringFilm", "boringDescription",
                LocalDate.of(1996, 8, 20), 120, new ArrayList<>(genres),
                new Rating(2, "PG", "детям рекомендуется смотреть фильм с родителями")));
    }

    public void shouldGetFilms() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Assertions.assertEquals(filmStorage.getObjects().get(0), new Film(1, "boringFilm", "boringDescription",
                LocalDate.of(1996, 8, 20), 120, new ArrayList<>(genres),
                new Rating(2, "PG", "детям рекомендуется смотреть фильм с родителями")));
    }

    public void doesErrorAppearWhenAddAddLike() {
        filmStorage.addLike(1, 1);
    }

    public void doesErrorAppearWhenRemoveLike() {
        filmStorage.removeLike(1, 1);
    }

    public void shouldGetMostPopularFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Assertions.assertEquals(filmStorage.mostPopularFilm(10).get(0), new Film(1, "boringFilm",
                "boringDescription", LocalDate.of(1996, 8, 20), 120,
                new ArrayList<>(genres), new Rating(2, "PG", "детям рекомендуется смотреть фильм с родителями")));
    }

    public void shouldGetGenres() {
        Assertions.assertEquals(filmStorage.getGenres().size(), 6);
    }

    public void shouldGetCorrectGenreById() {
        Assertions.assertEquals(filmStorage.getGenreById(1), new Genre(1, "Комедия"));
    }

    public void shouldGetRatings() {
        Assertions.assertEquals(filmStorage.getRatings().size(), 5);
    }

    public void shouldGetCorrectRatingById() {
        Assertions.assertEquals(filmStorage.getRatingById(1), new Rating(1, "G",
                "У фильма нет возрастных ограничений"));
    }
}
