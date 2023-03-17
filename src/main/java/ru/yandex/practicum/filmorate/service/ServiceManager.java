package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ServiceManager {
    private final UserService userService;
    private final FilmService filmService;

    @Autowired
    public ServiceManager(UserService userService,FilmService filmService){
        this.userService = userService;
        this.filmService = filmService;
    }
}
