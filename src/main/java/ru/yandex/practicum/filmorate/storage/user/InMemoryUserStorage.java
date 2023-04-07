package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (!userMap.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден в сервисе");
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
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        }
        return userMap.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);

        if (user == null) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден в сервисе");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователь с Id '" + friendId + "' не найден в сервисе");
        } else {
            user.addFriend(friendId);
            friend.addFriend(userId);
            userMap.put(user.getId(), user);
            userMap.put(friend.getId(), friend);
            log.info("Пользователь с Id '" + userId + " и пользователь с Id '" + friendId + " теперь друзья!");
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);

        if (user == null) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден в сервисе");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователь с Id '" + friendId + "' не найден в сервисе");
        } else {
            user.deleteFriend(friendId);
            friend.deleteFriend(userId);
            userMap.put(user.getId(), user);
            userMap.put(friend.getId(), friend);
            log.info("Пользователь с Id '" + userId + " и пользователь с Id '" + friendId + " больше не друзья!");
        }
    }

    @Override
    public List getUserFriends(long id) {
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        } else {
            List<Long> idList = List.of(1L, 2L);
            return userMap.get(id).getFriends().stream()
                    .map(userId -> userMap.get(userId)).collect(Collectors.toList());
        }
    }

    @Override
    public List getCommonFriends(long id, long otherId) {
        if (userMap.containsKey(id) && userMap.containsKey(otherId)) {
            return userMap.get(id).getFriends().stream()
                    .filter(userMap.get(otherId).getFriends()::contains)
                    .map(userId -> userMap.get(userId))
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("У пользователей с id " + id + " и " + otherId + " нет общих друзей");
        }
    }
}