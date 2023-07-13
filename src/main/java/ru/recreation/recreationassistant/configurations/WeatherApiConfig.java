package ru.recreation.recreationassistant.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("config.properties")
public class WeatherApiConfig {
    private static final String URL_API = "https://api.weather.yandex.ru/v2/forecast";
    @Value("${weather_api_key}")
    private String apiKey;
    @Value("${weather_api_key_name}")
    private String apiKeyName;

    public String getApiKey() {
        return apiKey;
    }
    public String getUrlApi(){
        return URL_API;
    }
    public String getApiKeyName()
    {
        return apiKeyName;
    }
}
