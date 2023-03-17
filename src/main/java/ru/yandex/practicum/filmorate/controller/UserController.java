package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ServiceManager;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ServiceManager serviceManager;
    private final ValidateService validateService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(ServiceManager serviceManager, ValidateService validateService) {
        this.serviceManager = serviceManager;
        this.validateService = validateService;
        this.userStorage = serviceManager.getUserService().getUserStorage();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userStorage.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) throws ValidationException {
        validateService.validateUser(user);
        userStorage.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws ValidationException, UserNotFoundException {
        validateService.validateUser(user);
        userStorage.updateUser(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id,
                          @PathVariable Integer friendId) throws ValidationException, UserNotFoundException {
        serviceManager.getUserService().addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id,
                             @PathVariable Integer friendId) throws UserNotFoundException {
        serviceManager.getUserService().deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        return serviceManager.getUserService().getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return serviceManager.getUserService().getMutualFriends(id, otherId);
    }
}
