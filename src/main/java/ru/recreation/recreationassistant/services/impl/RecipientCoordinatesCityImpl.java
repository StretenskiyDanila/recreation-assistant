package ru.recreation.recreationassistant.services.impl;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.services.RecipientCoordinatesCity;

@Service
@PropertySource("config.properties")
@Slf4j
public class RecipientCoordinatesCityImpl implements RecipientCoordinatesCity {
    private static final String URL = "https://geocode-maps.yandex.ru/1.x/";

    private final RestTemplateWork restTemplateWork;

    @Value("${geocode-maps_key}")
    private String appKey;

    public RecipientCoordinatesCityImpl(RestTemplateWork restTemplateWork) {
        this.restTemplateWork = restTemplateWork;
    }

    @Override
    public City getCoordinates(String city) {
        log.info("CoordinatesCity getCoordinates method start");
        MultiValueMap<String, String> map = restTemplateWork.getParams(appKey, city);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplateWork.getResponse(URL, map, request);
        return parseResponse(response);
    }

    private City parseResponse(ResponseEntity<String> response) {
        JsonObject jsonObject = restTemplateWork.getGsonResult(response, JsonObject.class);
        String[] coords = jsonObject.get("response").getAsJsonObject()
                .get("GeoObjectCollection").getAsJsonObject()
                .get("featureMember").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("GeoObject").getAsJsonObject()
                .get("Point").getAsJsonObject()
                .get("pos").getAsString().split(" ");
        double lat = Double.parseDouble(coords[0]);
        double lon = Double.parseDouble(coords[1]);
        return new City(lat, lon);
    }
}
