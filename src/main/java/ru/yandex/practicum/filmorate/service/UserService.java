package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователь с Id '" + friendId + "' не найден");
        } else {
            user.addFriend(friendId);
            friend.addFriend(userId);
            log.info("Пользователь с Id '" + userId + " и пользователь с Id '" + friendId + " теперь друзья!");
        }
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователь с Id '" + friendId + "' не найден");
        } else {
            user.deleteFriend(friendId);
            friend.deleteFriend(userId);
            log.info("Пользователь с Id '" + userId + " и пользователь с Id '" + friendId + " больше не друзья!");
        }
    }

    public List getUserFriends(long id) {
        if (!userStorage.isContains(id)) {
            throw new UserNotFoundException("Пользователь с Id '" + id + "' не найден в сервисе");
        } else {
            return userStorage.getById(id).getFriends().stream()
                    .map(userId -> userStorage.getById(userId)).collect(Collectors.toList());
        }
    }

    public List getCommonFriends(long id, long otherId) {
        if (userStorage.isContains(id) && userStorage.isContains(otherId)) {
            return userStorage.getById(id).getFriends().stream()
                    .filter(userStorage.getById(otherId).getFriends()::contains)
                    .map(userId -> userStorage.getById(userId))
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("У пользователей с id " + id + " и " + otherId + " нет общих друзей");
        }
    }
}
