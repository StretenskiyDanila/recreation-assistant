package ru.recreation.recreationassistant.services;

import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.utils.StationarySurveyStreet;

import java.util.Optional;

public interface UserService {


    User update(User user);

    Optional<User> getUser(Long telegramId);

    void setCity(User user, String city);

    void setCurrentState(User user, StationarySurveyStreet state);

    void addHealthTag(User user, Health health);

    void addDishTag(User user, Dish health);

    void addMealTag(User user, Meal health);

    void addCuisineTag(User user, Cuisine health);

    void setUsername(User user, String username);

    User save(User user, String username, Long telegramId);

    void clearUserTags(User user);
}
