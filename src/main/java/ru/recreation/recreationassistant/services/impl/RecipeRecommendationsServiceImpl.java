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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.models.Recipe;
import ru.recreation.recreationassistant.models.RecipeHits;
import ru.recreation.recreationassistant.services.RecipeRecommendationsService;
import ru.recreation.recreationassistant.services.TranslationService;
import ru.recreation.recreationassistant.utils.ListUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("config.properties")
public class RecipeRecommendationsServiceImpl implements RecipeRecommendationsService {

    private static final String URL = "https://api.edamam.com/api/recipes/v2";


    private final TranslationService translationService;

    @Value("${edamam.app_id}")
    private String appId;
    @Value("${edamam.app_key}")
    private String appKey;

    @Value("${edamam.food_limit}")
    private int recipeLimit;

    public RecipeRecommendationsServiceImpl(TranslationService translationService) {
        this.translationService = translationService;
    }


    @Override
    public List<Recipe> getRecipeRecommendations(User user, String userRequest) {
        try {
            log.info("RecipeRecomService getRecipeRecommendations method start");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            log.info("Looking for appId and appKey in config.properties ...");
            map.add("app_id", appId);
            map.add("app_key", appKey);
            map.add("type", "public");
            Set<Health> healthTags = user.getHealthTags();
            Set<Meal> mealTags = user.getMealTags();
            Set<Cuisine> cuisineTags = user.getCuisineTags();
            Set<Dish> dishTags = user.getDishTags();
            log.info("Addins params to request ...");
            if (StringUtils.hasText(userRequest)) {
                map.add("q", translationService.translate(userRequest, "ru", "en"));
            }
            if (!healthTags.isEmpty()) {
                map.addAll("health", healthTags.stream().map(Health::getHealthLabel).collect(Collectors.toList()));
            }
            if (!cuisineTags.isEmpty()) {
                map.addAll("cuisineType", cuisineTags.stream().map(Cuisine::getCuisineLabel).collect(Collectors.toList()));
            }
            if (!mealTags.isEmpty()) {
                map.addAll("mealType", mealTags.stream().map(Meal::getMealLabel).collect(Collectors.toList()));
            }
            if (!dishTags.isEmpty()) {
                map.addAll("dishType", dishTags.stream().map(Dish::getDishLabel).collect(Collectors.toList()));
            }


            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL).queryParams(map);
            UriComponents components = builder.build().encode();

            URI uri = components.toUri();
            log.info("Making request ...");
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
            RecipeHits hits;
            hits = mapper.readValue(response.getBody(), RecipeHits.class);

            if (hits != null) {
                return ListUtils.pickNRandom(hits.hits, recipeLimit).stream()
                        .map(recipeRecommendation -> recipeRecommendation.recipe).collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException during readValue from response");
            throw new RuntimeException();
        }
    }


}
