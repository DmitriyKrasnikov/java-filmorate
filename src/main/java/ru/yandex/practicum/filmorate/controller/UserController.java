package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final ValidateService validateService = new ValidateService();
    private final UserRepository userRepository = new UserRepository();

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(userRepository.getUsers().values());
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) throws ValidationException {
        validateService.validateUser(user);
        userRepository.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws ValidationException {
        validateService.validateUser(user);
        userRepository.updateUser(user);
        return user;
    }
}
