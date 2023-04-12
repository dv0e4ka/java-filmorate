package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> userMap = new HashMap<>();
    private long id;

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        userMap.put(user.getId(), user);
        log.info("добавлен пользователь " + user.getName());
        return user;
    }


    @Override
    public User update(User user) {
        long userId = user.getId();
        if (!isContains(userId)) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        log.info("обновлён пользователь" + user.getName());
        return user;
    }

    @Override
    public void delete(long id) {
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        } else {
            userMap.remove(id);
            log.info("Пользователь с Id '" + id + "' удалён");
        }
    }

    @Override
    public User getById(long id) {
        if (!isContains(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        }
        return userMap.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public boolean isContains(long id) {
        return userMap.containsKey(id);
    }
}