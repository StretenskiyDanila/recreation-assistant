package ru.recreation.recreationassistant.services;

import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.models.Event;

import java.util.List;

public interface SearchEventService {
    List<Event> getRecommendation(User user, String category);
}
