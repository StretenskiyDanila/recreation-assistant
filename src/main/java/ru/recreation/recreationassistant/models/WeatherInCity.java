package ru.recreation.recreationassistant.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class WeatherInCity {

    private FactWeather fact;

}
