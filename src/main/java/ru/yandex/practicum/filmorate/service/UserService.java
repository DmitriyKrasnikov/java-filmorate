package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDaoStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Slf4j
@Getter
public class UserService {

    private final UserDaoStorage userStorage;

    @Autowired
    public UserService(UserDaoStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        if (user == null) {
            log.debug("Передан пустой объект: {}", (Object) null);
            throw new ValidationException("Передан пустой объект");
        }
        return userStorage.addObject(user);
    }

    public void updateUser(User user) throws ValidationException, UserNotFoundException {
        if (user == null) {
            log.debug("Передан пустой объект: {}", (Object) null);
            throw new ValidationException("Передан пустой объект");
        } else {
            userStorage.updateObject(user);
        }
    }

    public User getUserById(int id) throws UserNotFoundException {
        return userStorage.getObjectById(id);
    }

    public List<User> getUsers() {
        return userStorage.getObjects();
    }

    public void addFriend(int id, int friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> getMutualFriends(int id, int friendId) {
        return userStorage.getMutualFriends(id, friendId);
    }

}
