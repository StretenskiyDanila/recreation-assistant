package ru.recreation.recreationassistant.services;

import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.models.City;

public interface RecipientCoordinatesCity {
    City getCoordinates(User user);
}
