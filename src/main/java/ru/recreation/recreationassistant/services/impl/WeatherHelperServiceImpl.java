package ru.recreation.recreationassistant.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jvnet.hk2.annotations.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.models.Forecast;
@Service
public class WeatherHelperServiceImpl{

    public ResponseEntity<String> makeRequest(City city)
    {
        String URL_API = "https://api.weather.yandex.ru/v2/forecast";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String key = "229641a8-814e-4e95-8832-a7325a9520dd", name ="X-Yandex-API-Key";
        headers.add(name, key);
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder sb = new StringBuilder(URL_API);
        sb.append("?lat=").append(city.getLatitude());
        sb.append("?lon=").append(city.getLongitude());
        sb.append("&extra=true");
        return restTemplate.exchange(sb.toString(), HttpMethod.GET, request, String.class);
    }

    public Forecast getForecast(ResponseEntity<String> json)
    {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json.getBody(), JsonObject.class);
        String condition = jsonObject.get("condition").getAsString();
        double feels_like = jsonObject.get("feels_like").getAsDouble();
        double temp_min = jsonObject.get("temp_min").getAsDouble();
        double temp_max = jsonObject.get("temp_max").getAsDouble();
        return new Forecast(temp_min, temp_max, feels_like, condition);
    }

    public String getRecomendation(Forecast weather)
    {
        return null;
    }
}
