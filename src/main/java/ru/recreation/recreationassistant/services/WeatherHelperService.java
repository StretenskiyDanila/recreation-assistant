package ru.recreation.recreationassistant.services;

import ru.recreation.recreationassistant.models.City;

public interface WeatherHelperService
{
    String makeRequest(City city);
}
