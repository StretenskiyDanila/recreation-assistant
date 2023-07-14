package ru.recreation.recreationassistant.services.impl;

import org.springframework.stereotype.Service;
import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.repositories.UserRepository;
import ru.recreation.recreationassistant.services.UserService;
import ru.recreation.recreationassistant.utils.StationarySurveyState;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(Long telegramId) {
        return userRepository.findByTelegramChatId(String.valueOf(telegramId)).get();
    }

    @Override
    public void setCity(User user, String city) {
        user.setCity(city);
        update(user);
    }

    @Override
    public void setCurrentState(User user, StationarySurveyState state) {
        user.setCurrentState(state.name());
        update(user);
    }

    @Override
    public void addHealthTag(User user, Health health) {
        user.getHealthTags().add(health);
        update(user);
    }

    @Override
    public void addDishTag(User user, Dish dish) {
        user.getDishTags().add(dish);
        update(user);
    }

    @Override
    public void addMealTag(User user, Meal meal) {
        user.getMealTags().add(meal);
        update(user);
    }

    @Override
    public void addCuisineTag(User user, Cuisine cuisine) {
        user.getCuisineTags().add(cuisine);
        update(user);
    }

    @Override
    public void setUsername(User user, String username) {
        user.setUsername(username);
        update(user);
    }

    @Override
    public void save(User user, String username, Long telegramId) {
        user.setUsername(username);
        user.setTelegramChatId(String.valueOf(telegramId));
        update(user);
    }

    @Override
    public void clearUserTags(User user) {
        user.getCuisineTags().clear();
        user.getMealTags().clear();
        user.getHealthTags().clear();
        user.getDishTags().clear();

        userRepository.saveAndFlush(user);
    }
}
