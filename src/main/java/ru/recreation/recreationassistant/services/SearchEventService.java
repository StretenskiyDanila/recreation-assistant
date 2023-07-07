package ru.recreation.recreationassistant.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.recreation.recreationassistant.models.Event;

import java.util.List;

public interface SearchEventService {
    List<Event> makingRequest(String location, String category) throws JsonProcessingException;
}
