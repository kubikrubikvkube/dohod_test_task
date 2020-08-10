package ru.dohod.testtask.exception;

public class ClientNotFoundException extends Exception {

    public ClientNotFoundException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
