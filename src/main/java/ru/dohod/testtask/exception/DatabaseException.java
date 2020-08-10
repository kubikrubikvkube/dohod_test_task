package ru.dohod.testtask.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
