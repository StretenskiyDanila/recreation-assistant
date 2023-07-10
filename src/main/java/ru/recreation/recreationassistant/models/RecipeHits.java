package ru.recreation.recreationassistant.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeHits {

    public int from;
    public int to;
    public int count;
    public List<RecipeRecommendation> hits;

}
