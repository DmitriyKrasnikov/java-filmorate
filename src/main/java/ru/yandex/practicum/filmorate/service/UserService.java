package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
@Getter
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addUser(User user) throws ValidationException {
        if (user == null) {
            log.debug("Передан пустой объект: {}", (Object) null);
            throw new ValidationException("Передан пустой объект");
        }
        if (userStorage.getUsers().containsValue(user)) {
            log.debug("Пользователь уже существует: {}", user);
            throw new ValidationException("Пользователь уже существует");
        }
        userStorage.addUser(user);
    }

    public void updateUser(User user) throws ValidationException, UserNotFoundException {
        if (user == null) {
            log.debug("Передан пустой объект: {}", (Object) null);
            throw new ValidationException("Передан пустой объект");
        }
        if (userStorage.getUsers().isEmpty() || !userStorage.getUsers().containsKey(user.getId())) {
            log.debug("Список пуст, либо пользователь: {} не существует", user);
            throw new UserNotFoundException("Ошибка при обновлении пользователя");
        } else {
            userStorage.updateUser(user);
        }
    }

    public User getUserById(int id) throws UserNotFoundException {
        if (userStorage.getUsers().isEmpty() || !userStorage.getUsers().containsKey(id)) {
            log.debug("Список пуст, либо пользователь: {} не существует", id);
            throw new UserNotFoundException("Ошибка при обновлении пользователя");
        } else {
            return userStorage.getUserById(id);
        }
    }

    public HashMap<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(int id, int friendId) throws ValidationException, UserNotFoundException {
        isExist(id);
        isExist(friendId);
        alreadyFriends(id, friendId);
        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) throws UserNotFoundException {
        isExist(id);
        isExist(friendId);
        notFriends(id, friendId);
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
    }

    public List<User> getFriends(int id) {
        isExist(id);
        List<User> friends = new ArrayList<>();
        for (int UserId : userStorage.getUsers().get(id).getFriends()) {
            friends.add(userStorage.getUsers().get(UserId));
        }
        return friends;
    }

    public List<User> getMutualFriends(int id, int friendId) throws UserNotFoundException {
        isExist(id);
        isExist(friendId);
        Set<Integer> mutualFriendsId = new HashSet<>(userStorage.getUsers().get(id).getFriends());
        mutualFriendsId.retainAll(userStorage.getUsers().get(friendId).getFriends());
        List<User> mutualFriends = new ArrayList<>();
        for (int UserId : mutualFriendsId) {
            mutualFriends.add(userStorage.getUsers().get(UserId));
        }
        return mutualFriends;
    }

    public void isExist(int id) throws UserNotFoundException {
        if (!userStorage.getUsers().containsKey(id)) {
            log.debug("Пользователь с указанным id не найден: {}", id);
            throw new UserNotFoundException("Пользователь с указанным id не найден");
        }
    }

    private void alreadyFriends(int id, int friendId) throws ValidationException {
        if (userStorage.getUsers().get(id).getFriends().contains(friendId)) {
            log.debug("Пользователь {} уже добавлен в друзья {}", friendId, id);
            throw new ValidationException("Пользователи уже друзья");
        }
        if (userStorage.getUsers().get(friendId).getFriends().contains(id)) {
            log.debug("Пользователь {} уже добавлен в друзья {}", id, friendId);
            throw new ValidationException("Пользователи уже друзья");
        }
    }

    private void notFriends(int id, int friendId) throws UserNotFoundException {
        if (!userStorage.getUsers().get(id).getFriends().contains(friendId)) {
            log.debug("Пользователь {} не добавлен в друзья {}", friendId, id);
            throw new UserNotFoundException("Пользователи не добавлены в друзья");
        }
        if (!userStorage.getUsers().get(friendId).getFriends().contains(id)) {
            log.debug("Пользователь {} не добавлен в друзья {}", id, friendId);
            throw new UserNotFoundException("Пользователи не добавлены в друзья");
        }
    }
}
