package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.recreation.recreationassistant.configurations.ApiConfig;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.models.FactWeather;
import ru.recreation.recreationassistant.models.Recommendation;
import ru.recreation.recreationassistant.models.WeatherInCity;
import ru.recreation.recreationassistant.services.WeatherHelperService;

@Service
public class WeatherHelperServiceImpl implements WeatherHelperService {
    private ApiConfig apiConfig;

    public String getRecommendation(City city) throws JsonProcessingException {
        String URL_API = "https://api.weather.yandex.ru/v2/forecast";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String key = apiConfig.getApiKey(), name = apiConfig.getApiKeyName();
        headers.add(name, key);
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        String sb = URL_API + "?lat=" + city.getLatitude() +
                "&lon=" + city.getLongitude() +
                "&extra=true";
        return getStringRecommendation(getForecast(restTemplate.exchange(sb, HttpMethod.GET, request, String.class)));
    }

    private FactWeather getForecast(ResponseEntity<String> json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        WeatherInCity weather = mapper.readValue(json.getBody(), WeatherInCity.class);
        return weather.fact;
    }

    private String getStringRecommendation(FactWeather weather) {
        StringBuilder result = new StringBuilder();
        if (weather.feels_like > 17) {
            result.append(Recommendation.WARM_INFO).append(Recommendation.TEMP_FEELS).append(weather.feels_like).append("°C. ");
        } else {
            result.append(Recommendation.COLD_WARNING).append(Recommendation.TEMP_FEELS).append(weather.feels_like).append("°C. ");
        }
        if (weather.condition.equals("rain")) {
            result.append(Recommendation.RAIN_WARNING);
        }
        return result.toString();
    }
}
