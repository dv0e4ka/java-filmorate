package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private HashMap<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        if (validateUser(user)) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(++id);
            userMap.put(user.getId(), user);
            log.info("добавлен пользователь " + user.getName());
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        int userId = user.getId();
        log.info("получен запрос на обновление пользователя с id " + userId);
        if (validateUser(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (userMap.containsKey(userId)) {
                userMap.put(user.getId(), user);
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
        return new ArrayList(userMap.values());
    }

    private boolean validateUser(User user) throws ValidationException {
        return true;
    }
}