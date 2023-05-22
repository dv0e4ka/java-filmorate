package ru.yandex.practicum.filmorate.exception;

public class ReviewHasNotBeenRatedException extends RuntimeException {
    public ReviewHasNotBeenRatedException(String message) {
        super(message);
    }
}