package ru.recreation.recreationassistant.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Recipe {

    private String label;
    private List<String> ingredientLines;
    private String calories;
    private double totalTime;
    private List<String> cuisineType;

    private String url;

}
