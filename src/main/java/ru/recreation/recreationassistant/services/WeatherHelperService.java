package ru.recreation.recreationassistant.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.recreation.recreationassistant.models.City;

public interface WeatherHelperService
{
    String getRecommendation(City city) throws JsonProcessingException;
}
