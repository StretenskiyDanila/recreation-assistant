package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.services.TranslationService;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestTemplateWork {

    public ResponseEntity<String> getResponse(String URL, MultiValueMap<String, String> map,
                                              HttpEntity<MultiValueMap<String, String>> request) {
        log.info("Making request ...");
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL).queryParams(map);
        UriComponents components = builder.build().encode();
        return restTemplate.exchange(components.toUri(), HttpMethod.GET, request, String.class);
    }

    public <T> T getJacksonResult(ResponseEntity<String> response, Class<T> valueType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), valueType);
    }

    public <T> T getGsonResult(ResponseEntity<String> response, Class<T> valueType) {
        log.info("Parsing response...");
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), valueType);
    }

    public MultiValueMap<String, String> getParams(String appId, String appKey, User user, String userRequest, TranslationService translationService) {
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
        return map;
    }

    public MultiValueMap<String, String> getParams(String appKey, String city) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        log.info("Looking for apikey and geocode in config.properties ...");
        map.add("apikey", appKey);
        map.add("geocode", city);
        map.add("format", "json");
        return map;
    }

    public MultiValueMap<String, String> getParams(User user, String category) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        log.info("Parsing user and category params ...");
        map.add("text_format", "text");
        map.add("location", user.getCity());
        map.add("categories", category);
        return map;
    }

    public MultiValueMap<String, String> getParams(String from, String to, String line) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client", "gtx");
        map.add("sl", from);
        map.add("tl", to);
        map.add("dt", "t");
        map.add("q", line);
        return map;
    }

    public MultiValueMap<String, String> getParams(City city) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("lat", String.valueOf(city.getLatitude()));
        map.add("lon", String.valueOf(city.getLongitude()));
        map.add("extra", "true");
        return map;
    }

}
