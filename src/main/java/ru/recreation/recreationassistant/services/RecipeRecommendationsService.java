package ru.recreation.recreationassistant.services;

import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.models.Recipe;

import java.util.List;

public interface RecipeRecommendationsService {

    List<Recipe> getRecipeRecommendations(User user, String userRequest);

}
