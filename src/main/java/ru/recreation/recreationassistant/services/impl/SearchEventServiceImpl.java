package ru.recreation.recreationassistant.services.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ru.recreation.recreationassistant.services.SearchEventService;

@Service
public class SearchEventServiceImpl implements SearchEventService {
    private final String URL = "https://kudago.com/public-api/v1.4/places/";

    public ResponseEntity<String> makingRequest(String location, String category) {
        RestTemplate restTemplate = new RestTemplate();
        String url_request = String.format("%s?text_format=text&location=%s&categories=%s", URL, location, category);
        return restTemplate.getForEntity(url_request, String.class);
    }
}
