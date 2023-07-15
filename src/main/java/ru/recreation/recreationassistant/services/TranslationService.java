package ru.recreation.recreationassistant.services;

public interface TranslationService {

    String translate(String line);
    String translate(String line, String from, String to);

}
