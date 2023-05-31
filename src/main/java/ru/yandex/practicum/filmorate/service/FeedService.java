package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.EventNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.Review;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class FeedService {
    private final FeedDao feedDao;
    private final UserDao userDao;

    @Autowired
    public FeedService(FeedDao feedDao, UserDao userDao) {
        this.feedDao = feedDao;
        this.userDao = userDao;
    }

    public void addEvent(Event event) {
        feedDao.addEvent(event);
    }

    public Event getFeedById(Long eventId) {
        if (eventId == null) {
            throw new IncorrectParameterException("Некорректные входные данные");
        } else {
            log.info("Получен запрос на возврат события с Id '" + eventId);
            Event result = feedDao.getEvent(eventId);
            if (result == null)
                throw new EventNotFoundException("Событие с Id '" + eventId + "' не найдено");
            else
                return result;
        }
    }

    public List<Event> getFeed(Long userId) {
        if (userId == null) {
            throw new IncorrectParameterException("Некорректные входные данные");
        } else
        if (!userDao.isContains(userId)) {
            throw new UserNotFoundException("Пользователь с Id '" + userId + "' не найден");
        } else {
            log.info("Получен запрос на ленту событий пользователя с Id '" + userId);
            return feedDao.getEvents(userId);
        }

    }

    public void addFriendEvent(long userId, long friendId) {
        //Нет проверок Id, тк они происходят в UserService
        //Если архитектура поменяется, то надо будет здесь проверять входные данные.
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.FRIEND).
                operation(OperationType.ADD).
                entityId(friendId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void deleteFriendEvent(long userId, long friendId) {
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.FRIEND).
                operation(OperationType.REMOVE).
                entityId(friendId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void addLikeEvent(long userId, long filmId) {
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.LIKE).
                operation(OperationType.ADD).
                entityId(filmId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void deleteLikeEvent(long userId, long filmId) {
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.LIKE).
                operation(OperationType.REMOVE).
                entityId(filmId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void addReviewEvent(Review review) {
        Event event = Event.builder().
                userId(review.getUserId()).
                eventType(EventType.REVIEW).
                operation(OperationType.ADD).
                entityId(review.getReviewId()).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void deleteReviewEvent(Review review) {
        Event event = Event.builder().
                userId(review.getUserId()).
                eventType(EventType.REVIEW).
                operation(OperationType.REMOVE).
                entityId(review.getReviewId()).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void updateReviewEvent(Review review) {
        Event event = Event.builder().
                userId(review.getUserId()).
                eventType(EventType.REVIEW).
                operation(OperationType.UPDATE).
                entityId(review.getReviewId()).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }
}
