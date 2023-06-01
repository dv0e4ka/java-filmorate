package ru.yandex.practicum.filmorate.exception;

public class ReviewHasAlreadyBeenRatedException extends RuntimeException {
    public ReviewHasAlreadyBeenRatedException(String message) {
        super(message);
    }
}