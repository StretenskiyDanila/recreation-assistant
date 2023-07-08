package ru.recreation.recreationassistant.services;

import org.springframework.http.ResponseEntity;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.models.Forecast;

import java.util.List;

public interface WeatherHelperService
{

    ResponseEntity<String> makeRequest(City city);
    Forecast getForecast(ResponseEntity<String> json);
    String getRecomendation(Forecast weather);

}
