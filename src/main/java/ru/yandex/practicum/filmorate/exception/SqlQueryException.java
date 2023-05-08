package ru.yandex.practicum.filmorate.exception;

public class SqlQueryException extends RuntimeException {
    public SqlQueryException(String message) {
        super(message);
    }
}
