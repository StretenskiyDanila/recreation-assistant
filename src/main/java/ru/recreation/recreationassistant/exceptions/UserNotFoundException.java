package ru.recreation.recreationassistant.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long id) {
        super("Пользователь с telegram chat id " + id + " не найден.");
    }

}
