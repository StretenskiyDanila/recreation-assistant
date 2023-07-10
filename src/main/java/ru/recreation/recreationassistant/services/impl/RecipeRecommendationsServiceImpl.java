package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.models.Recipe;
import ru.recreation.recreationassistant.models.RecipeHits;
import ru.recreation.recreationassistant.services.RecipeRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("config.properties")
public class RecipeRecommendationsServiceImpl implements RecipeRecommendationsService {

    private static final String URL = "https://api.edamam.com/api/recipes/v2";

    @Value("${edamam.app_id}")
    private String appId;
    @Value("${edamam.app_key}")
    private String appKey;


    @Override
    public List<Recipe> getRecipeRecommendations(User user, String userRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("app_id", appId);
        map.add("app_key", appKey);
        map.add("q", userRequest);
        map.add("type", "public");
        map.addAll("health", user.getHealthTags().stream().map(Health::getHealthLabel).collect(Collectors.toList()));
        map.addAll("cuisineType", user.getCuisineTags().stream().map(Cuisine::getCuisineLabel).collect(Collectors.toList()));
        map.addAll("mealType", user.getMealTags().stream().map(Meal::getMealLabel).collect(Collectors.toList()));
        map.addAll("dishType", user.getDishTags().stream().map(Dish::getDishLabel).collect(Collectors.toList()));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> response = restTemplate.exchange("https://api.edamam.com/api/recipes/v2?app_id=e487db9a&app_key=731b4ebfd3231254846b6ff6ec36564d&type=public&q=chicken", HttpMethod.GET, request, String.class);
        RecipeHits hits;
        try {
            hits = mapper.readValue(response.getBody(), RecipeHits.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (hits != null) {
            return hits.hits.stream().map(recipeRecommendation -> recipeRecommendation.recipe).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
