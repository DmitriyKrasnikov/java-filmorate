package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class UserDao implements UserDaoStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addObject(User user) {
        user.setId(getTableId() + 1);
        jdbcTemplate.update("INSERT INTO user_data (id, login, name, birthday, email) VALUES(?,?,?,?,?)",
                user.getId(), user.getLogin(), user.getName(), user.getBirthday().toString(), user.getEmail());

        return user;
    }

    @Override
    public void updateObject(User user) {
        getObjectById(user.getId());
        jdbcTemplate.update("UPDATE user_data SET login = ?, name = ?,  birthday = ?, email = ? WHERE id = ?",
                user.getLogin(), user.getName(), user.getBirthday().toString(), user.getEmail(), user.getId());
    }

    @Override
    public User getObjectById(int id) {
        return jdbcTemplate.query("SELECT * FROM user_data WHERE id = ?", (rs, rowNum) -> makeUser(rs), id).stream()
                .findAny().orElseThrow(() -> new UserNotFoundException("Пользователь с указанным id " + id + " не найден"));
    }

    private Integer getTableId() {
        return jdbcTemplate.query("SELECT * FROM user_data ORDER BY id DESC LIMIT 1 ", (rs, rowNum) ->
                rs.getInt("id")).stream().findAny().orElse(0);
    }

    @Override
    public List<User> getObjects() {
        return jdbcTemplate.query("SELECT * FROM user_data", (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        String email = rs.getString("email");

        return new User(id, login, name, birthday, email);
    }

    @Override
    public void addFriend(int id, int friendId) {
        getObjectById(id);
        getObjectById(friendId);
        boolean isPresent = jdbcTemplate.query("SELECT status FROM friends WHERE user_id = ? AND friend_user_id = ?",
                (rs, rowNum) -> rs.getBoolean("status"), friendId, id).stream().findAny().isPresent();
        if (isPresent) {
            jdbcTemplate.update("INSERT INTO friends VALUES(?,?,?)", id, friendId, true);
        } else {
            jdbcTemplate.update("INSERT INTO friends VALUES(?,?,?)", id, friendId, false);
        }
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_user_id = ?", id, friendId);
        if (jdbcTemplate.query("SELECT status FROM friends WHERE user_id = ? AND friend_user_id = ? ",
                (rs, rowNum) -> rs.getBoolean("status"), friendId, id).stream().findAny().isPresent()) {
            jdbcTemplate.update("UPDATE friends SET status = ? WHERE user_id = ? AND friend_user_id = ?",
                    friendId, id, false);
        }
    }

    @Override
    public List<User> getFriends(int id) {
        return jdbcTemplate.query("SELECT * FROM user_data WHERE id IN (SELECT friend_user_id AS id FROM friends" +
                " WHERE user_id = ?) ORDER BY id ", (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public List<User> getMutualFriends(int id, int friendId) {
        return jdbcTemplate.query("SELECT * FROM user_data WHERE id IN (SELECT id FROM (SELECT friend_user_id AS id" +
                " FROM friends WHERE user_id = ?) AS i INNER JOIN (SELECT friend_user_id FROM friends WHERE user_id = ?)" +
                " AS j ON i.id = j.friend_user_id) ORDER BY id", (rs, rowNum) -> makeUser(rs), id, friendId);
    }
}
