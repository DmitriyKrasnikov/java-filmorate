package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    @NotNull
    private int id;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
    @NotBlank
    @Email
    private final String email;
    private Set<Integer> friends;
}


