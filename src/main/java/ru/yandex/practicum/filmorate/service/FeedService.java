package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class FeedService {
    private final FeedDao feedDao;

    @Autowired
    public FeedService(FeedDao feedDao) {
        this.feedDao = feedDao;
    }

    public void addEvent(Event event) {
        feedDao.addEvent(event);
    }

    public Event getFeedById(long eventId) {
        return feedDao.getEvent(eventId);
    }

    public List<Event> getFeed(long userId) {
        return feedDao.getEvents(userId);
    }

    public void addFriendEvent(long userId, long friendId) {
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.FRIEND).
                operationType(OperationType.ADD).
                entityId(friendId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void deleteFriendEvent(long userId, long friendId) {
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.FRIEND).
                operationType(OperationType.REMOVE).
                entityId(friendId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void addLikeEvent(long userId, long filmId) {
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.LIKE).
                operationType(OperationType.ADD).
                entityId(filmId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }

    public void deleteLikeEvent(long userId, long filmId) {
        Event event = Event.builder().
                userId(userId).
                eventType(EventType.LIKE).
                operationType(OperationType.REMOVE).
                entityId(filmId).
                timestamp(Instant.now().toEpochMilli()).
                build();
        feedDao.addEvent(event);
    }
}
