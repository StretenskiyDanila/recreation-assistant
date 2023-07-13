package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.models.FactWeather;
import ru.recreation.recreationassistant.models.Recommendation;
import ru.recreation.recreationassistant.models.WeatherInCity;
import ru.recreation.recreationassistant.services.WeatherHelperService;

@Service
@Slf4j
public class WeatherHelperServiceImpl implements WeatherHelperService {

    public String getRecommendation(City city) throws JsonProcessingException {
        log.info("WeatherService getRecommendation start");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("Adding api_data from config.properties");
        headers.add(API_KEY_NAME, API_KEY);
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        log.info("Forming url from city params ...");
        String sb = API_URL + "?lat=" + city.getLatitude() +
                "&lon=" + city.getLongitude() +
                "&extra=true";
        return getStringRecommendation(getForecast(restTemplate.exchange(sb, HttpMethod.GET, request, String.class)));
    }

    private FactWeather getForecast(ResponseEntity<String> json) throws JsonProcessingException {
        log.info("WeatherHelper getForecast method start");
        ObjectMapper mapper = new ObjectMapper();
        log.info("Parsing weather data from json ...");
        WeatherInCity weather = mapper.readValue(json.getBody(), WeatherInCity.class);
        return weather.fact;
    }

    private String getStringRecommendation(FactWeather weather) {
        log.info("WeatherHelper getStringRecommendation method start");
        StringBuilder result = new StringBuilder();
        log.info("Adding recommendations");
        if (weather.feels_like > 17) {
            result.append(Recommendation.WARM_INFO).append(Recommendation.TEMP_FEELS).append(weather.feels_like).append("°C. ");
        } else {
            result.append(Recommendation.COLD_WARNING).append(Recommendation.TEMP_FEELS).append(weather.feels_like).append(" °C. ");
        }
        if (weather.condition.equals("rain")) {
            result.append(Recommendation.RAIN_WARNING);
        }
        return result.toString();
    }
    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";
    @Value("${weather_api_key}")
    private String API_KEY;
    @Value("${weather_api_key_name}")
    private String API_KEY_NAME;

}
