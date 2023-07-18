package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.models.Event;
import ru.recreation.recreationassistant.services.SearchEventService;
import ru.recreation.recreationassistant.models.Exhibition;

import java.util.List;

@Service
@Slf4j
public class SearchEventServiceImpl implements SearchEventService {

    public List<Event> getRecommendation(User user, String category) {
        try {
            log.info("SearchEventService getRecommendation method start");
            String url = "https://kudago.com/public-api/v1.4/places/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            log.info("Parsing user and category params ...");
            map.add("text_format", "text");
            map.add("location", user.getCity());
            map.add("categories", category);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(map);
            UriComponents components = builder.build().encode();

            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            log.info("Making request ...");
            ResponseEntity<String> response = restTemplate.exchange(components.toUri(), HttpMethod.GET, request, String.class);
            Exhibition exhibition;
            log.info("Creating exhibition from respone body");
            exhibition = mapper.readValue(response.getBody(), Exhibition.class);
            return exhibition.getEventList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }
}