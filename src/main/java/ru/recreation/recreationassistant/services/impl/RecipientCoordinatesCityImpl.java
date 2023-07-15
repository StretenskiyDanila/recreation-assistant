package ru.recreation.recreationassistant.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.services.RecipientCoordinatesCity;

@Service
@PropertySource("config.properties")
@Slf4j
public class RecipientCoordinatesCityImpl implements RecipientCoordinatesCity {
    private static final String URL = "https://geocode-maps.yandex.ru/1.x/";

    @Value("${geocode-maps_key}")
    private String appKey;

    @Override
    public City getCoordinates(String city) {
        log.info("CoordinatesCity getCoordinates method start");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        log.info("Looking for apikey and geocode in config.properties ...");
        map.add("apikey", appKey);
        map.add("geocode", city);
        map.add("format", "json");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL).queryParams(map);
        UriComponents components = builder.build().encode();
        log.info("Making request ...");
        ResponseEntity<String> response = restTemplate.exchange(components.toUri(), HttpMethod.GET, request, String.class);
        log.info("Parsing response...");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);
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
