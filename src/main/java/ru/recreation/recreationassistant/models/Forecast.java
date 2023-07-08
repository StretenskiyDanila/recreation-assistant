package ru.recreation.recreationassistant.models;

public class Forecast
{
    public double temp_min;
    public double temp_max;
    public double feels_like;
    public String condition;

    public Forecast(double tempMin, double tempMax, double feelsLike, String condition)
    {
        this.temp_min = tempMin;
        this.temp_max = tempMax;
        this.feels_like = feelsLike;
        this.condition = condition;
    }
}
