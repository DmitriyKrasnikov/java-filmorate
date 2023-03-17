package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Getter
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        List<User> friends = new ArrayList<>();
        for (int UserId : userStorage.getUsers().get(id).getFriends()) {
            friends.add(userStorage.getUsers().get(UserId));
        }
        return friends;
    }

    public List<User> getMutualFriends(int id, int friendId) throws UserNotFoundException {
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
