package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.models.FactWeather;
import ru.recreation.recreationassistant.models.Recommendation;
import ru.recreation.recreationassistant.models.WeatherInCity;
import ru.recreation.recreationassistant.services.WeatherHelperService;

@Service
@Slf4j
public class WeatherHelperServiceImpl implements WeatherHelperService {

    private static final String URL = "https://api.weather.yandex.ru/v2/forecast";
    @Value("${weather_api_key}")
    private String API_KEY;
    @Value("${weather_api_key_name}")
    private String API_KEY_NAME;

    private final RestTemplateWork restTemplateWork;

    public WeatherHelperServiceImpl(RestTemplateWork restTemplateWork) {
        this.restTemplateWork = restTemplateWork;
    }

    public String getRecommendation(City city) throws JsonProcessingException {
        log.info("WeatherService getRecommendation start");
        MultiValueMap<String, String> map = restTemplateWork.getParams(city);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("Adding api_data from config.properties");
        headers.add(API_KEY_NAME, API_KEY);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplateWork.getResponse(URL, map, request);
        log.info("Parsing weather data from json ...");
        WeatherInCity weather = restTemplateWork.getJacksonResult(response, WeatherInCity.class);
        return getStringRecommendation(weather.getFact());
    }

    private String getStringRecommendation(FactWeather weather) {
        log.info("WeatherHelper getStringRecommendation method start");
        StringBuilder result = new StringBuilder();
        log.info("Adding recommendations");
        if (weather.getFeelsLike() > 17) {
            result.append(Recommendation.WARM_INFO).append(Recommendation.TEMP_FEELS).append(weather.getFeelsLike()).append("°C. ");
        } else {
            result.append(Recommendation.COLD_WARNING).append(Recommendation.TEMP_FEELS).append(weather.getFeelsLike()).append(" °C. ");
        }
        if (weather.getCondition().equals("rain")) {
            result.append(Recommendation.RAIN_WARNING);
        }
        return result.toString();
    }

}
