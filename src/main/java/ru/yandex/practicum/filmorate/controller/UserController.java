package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("получен запрос на добавления пользователя с id " + user.getId());
        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("получен запрос на обновление пользователя с id " + user.getId());
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(long userId) {
        log.info("получен запрос на удаление пользователя с id " + userId);
        userService.delete(userId);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) {
        log.info("получен запрос на получение пользователя с id " + id);
        return userService.getById(id);
    }

    @GetMapping
    public List getAllUsers() {
        log.info("получен запрос на получение все пользователей");
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("получен запрос на дружбу от пользователя " + id + " c пользователем " + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("получен запрос на удаление из друзьей от пользователя " + id + " пользователя " + friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List getFriends(@PathVariable("id") long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}