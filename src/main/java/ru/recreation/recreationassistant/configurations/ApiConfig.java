package ru.recreation.recreationassistant.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("weather_api.properties")
public class ApiConfig {

    @Value("${api_key}")
    private String apiKey;
    @Value("${api_key_name}")
    private String apiKeyName;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiKeyName()
    {
        return apiKeyName;
    }
}
