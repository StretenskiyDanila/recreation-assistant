package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.recreation.recreationassistant.models.Event;
import ru.recreation.recreationassistant.services.SearchEventService;
import ru.recreation.recreationassistant.models.Exhibition;

import java.util.List;

@Service
public class SearchEventServiceImpl implements SearchEventService {

    public List<Event> getRecommendation(@Nullable String location, @Nullable String category) throws JsonProcessingException {
        String url = "https://kudago.com/public-api/v1.4/places/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("text_format", "text");
        map.add("location", location);
        map.add("categories", category);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(map);
        UriComponents components = builder.build().encode();

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> response = restTemplate.exchange(components.toUri(), HttpMethod.GET, request, String.class);
        Exhibition exhibition = mapper.readValue(response.getBody(), Exhibition.class);
        return exhibition.results;
    }
}