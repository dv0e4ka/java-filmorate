package ru.yandex.practicum.filmorate.exception;

public class MpaTableEmptyException extends RuntimeException {
    public MpaTableEmptyException(String message) {
        super(message);
    }
}
