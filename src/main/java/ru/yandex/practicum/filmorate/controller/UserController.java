package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private HashMap<Integer, User> getUsers = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User addUser(@NonNull @RequestBody User user) throws ValidationException {
        if (validateUser(user)) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(++id);
            getUsers.put(user.getId(), user);
            log.info("добавлен пользователь {}" + user.getName());
        }
        return user;
    }

    @PutMapping
    public User updateUser(@NonNull @RequestBody User user) throws ValidationException {
        int userId = user.getId();
        log.info("получен запрос на обновление пользователя с id " + userId);
        if (validateUser(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (getUsers.containsKey(userId)) {
                getUsers.put(user.getId(), user);
                log.info("обновлён пользователь" + user.getName());
                return user;
            } else {
                String exception = "Пользователь с Id '" + userId + "' не найден в сервисе";
                log.error(exception);
                throw new ValidationException(exception);
            }
        }
        return null;
    }

    @GetMapping
    public List getUsers() {
        return new ArrayList(getUsers.values());
    }

    private boolean validateUser(User user) throws ValidationException {
        String userEmail = user.getEmail();
        if (userEmail.isBlank() || !userEmail.contains("@")) {
            String exception = "неккоректно введена почта пользователя";
            log.error(exception);
            throw new ValidationException(exception);
        }

        String userLogin = user.getLogin();
        if (userLogin.isBlank() || !userLogin.matches("\\S+")) {
            String exception = "неккоректно введен логин пользователя";
            log.error(exception);
            throw new ValidationException(exception);
        }

        LocalDate birthday = user.getBirthday();
        if(birthday.isAfter(LocalDate.now())) {
            String exception = "указана дата день рождения пользователя из будущего времени";
            log.error(exception);
            throw new ValidationException(exception);
        }
        return true;
    }
}
