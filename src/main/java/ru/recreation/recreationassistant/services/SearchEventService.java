package ru.recreation.recreationassistant.services;

import org.springframework.http.ResponseEntity;

public interface SearchEventService {
    ResponseEntity<String> makingRequest(String location, String category);
}
