package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.models.Recipe;
import ru.recreation.recreationassistant.models.RecipeHits;
import ru.recreation.recreationassistant.models.RecipeRecommendation;
import ru.recreation.recreationassistant.services.RecipeRecommendationsService;
import ru.recreation.recreationassistant.services.TranslationService;
import ru.recreation.recreationassistant.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("config.properties")
public class RecipeRecommendationsServiceImpl implements RecipeRecommendationsService {

    private static final String URL = "https://api.edamam.com/api/recipes/v2";

    private final TranslationService translationService;
    private final RestTemplateWork restTemplateWork;

    @Value("${edamam.app_id}")
    private String appId;
    @Value("${edamam.app_key}")
    private String appKey;

    @Value("${edamam.food_limit}")
    private int recipeLimit;

    public RecipeRecommendationsServiceImpl(TranslationService translationService, RestTemplateWork restTemplateWork) {
        this.translationService = translationService;
        this.restTemplateWork = restTemplateWork;
    }

    @Override
    public List<Recipe> getRecipeRecommendations(User user, String userRequest) {
        try {
            log.info("RecipeRecomService getRecipeRecommendations method start");
            MultiValueMap<String, String> map = restTemplateWork.getParams(appId, appKey, user, userRequest, translationService);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplateWork.getResponse(URL, map, request);
            RecipeHits hits = restTemplateWork.getJacksonResult(response, RecipeHits.class);
            if (hits != null) {
                return ListUtils.pickNRandom(hits.getHits(), recipeLimit).stream()
                        .map(RecipeRecommendation::getRecipe).collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException during readValue from response");
            throw new RuntimeException();
        }
    }

}
