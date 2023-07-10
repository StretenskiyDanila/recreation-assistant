package ru.recreation.recreationassistant.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {

    public String label;
    public String image;
    public List<String> ingredientLines;
    public String calories;
    public double totalTime;
    public List<String> cuisineType;
    public List<String> mealType;
    public List<String> dishType;

}
