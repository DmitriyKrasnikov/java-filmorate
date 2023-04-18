package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class FilmDao implements FilmDaoStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addObject(Film film) {
        film.setId(getTableId() + 1);
        jdbcTemplate.update("INSERT INTO film VALUES(?,?,?,?,?,?)", film.getId(), film.getName(),
                film.getDescription(), film.getReleaseDate().toString(), film.getDuration(), film.getMpa().getId());

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO film_genres VALUES(?,?)", film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public void updateObject(Film film) {
        getObjectById(film.getId());
        jdbcTemplate.update("UPDATE film SET name = ?, description = ?, realise_date = ?, duration = ?," +
                " rating_id = ? WHERE id = ?", film.getName(), film.getDescription(), film.getReleaseDate().toString(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        if (film.getGenres() != null) {
            jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO film_genres VALUES(?,?)", film.getId(), genre.getId());
            }
        }
    }

    @Override
    public Film getObjectById(int id) {
        return jdbcTemplate.query("SELECT * FROM film WHERE id = ? ", (rs, rowNum) -> makeFilm(rs), id).stream()
                .findAny().orElseThrow(() -> new FilmNotFoundException("Фильм с указанным id " + id + " не найден"));
    }

    private Integer getTableId() {
        return jdbcTemplate.query("SELECT * FROM film ORDER BY id DESC LIMIT 1 ", (rs, rowNum) ->
                rs.getInt("id")).stream().findAny().orElse(0);
    }

    @Override
    public List<Film> getObjects() {
        return jdbcTemplate.query("SELECT * FROM film", (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate realiseDate = rs.getDate("realise_date").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("rating_id");

        Film film = new Film(id, name, description, realiseDate, duration, null, null);
        film.setGenres(getFilmGenres(id));
        film.setMpa(getRatingById(ratingId));

        return film;
    }

    @Override
    public void addLike(int userId, int filmId) {
        jdbcTemplate.update("INSERT INTO likes VALUES(?,?)", userId, filmId);
    }

    @Override
    public void removeLike(int userId, int filmId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_like_id = ? AND film_id = ?", userId, filmId);
    }

    @Override
    public List<Film> mostPopularFilm(int count) {
        List<Film> popular = jdbcTemplate.query("SELECT * FROM film WHERE id IN (SELECT film_id AS id FROM likes " +
                "GROUP BY id ORDER BY COUNT(user_like_id) DESC LIMIT ?)", (rs, rowNum) -> makeFilm(rs), count);

        if (popular.isEmpty()) {
            return getObjects();
        }
        return popular;
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM genre ORDER BY id ", (rs, rowNum) -> makeGenre(rs));
    }

    private List<Genre> getFilmGenres(int id) {
        return jdbcTemplate.query("SELECT * FROM genre WHERE id IN (SELECT genre_id AS id FROM film_genres WHERE " +
                "film_id = ?)", (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public Genre getGenreById(int id) {
        return jdbcTemplate.query("SELECT * FROM genre WHERE id = ?", (rs, rowNum) -> makeGenre(rs), id).stream()
                .findAny().orElseThrow(() -> new FilmNotFoundException("Жанр с указанным id " + id + " не найден"));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }

    @Override
    public List<Rating> getRatings() {
        return jdbcTemplate.query("SELECT * FROM rating", (rs, rowNum) -> makeRating(rs));
    }

    @Override
    public Rating getRatingById(int id) {
        return jdbcTemplate.query("SELECT * FROM rating WHERE id = ?", (rs, rowNum) -> makeRating(rs), id).stream()
                .findAny().orElseThrow(() -> new FilmNotFoundException("Рейтинг с указанным id " + id + " не найден"));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");

        return new Rating(id, name, description);
    }
}
