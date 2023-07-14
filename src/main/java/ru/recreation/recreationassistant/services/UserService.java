package ru.recreation.recreationassistant.services;

import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.utils.StationarySurveyState;

public interface UserService {

    void update(User user);

    User getUser(Long telegramId);

    void setCity(User user, String city);

    void setCurrentState(User user, StationarySurveyState state);

    void addHealthTag(User user, Health health);

    void addDishTag(User user, Dish health);

    void addMealTag(User user, Meal health);

    void addCuisineTag(User user, Cuisine health);

    void setUsername(User user, String username);

    void save(User user, String username, Long telegramId);

    void clearUserTags(User user);

}
