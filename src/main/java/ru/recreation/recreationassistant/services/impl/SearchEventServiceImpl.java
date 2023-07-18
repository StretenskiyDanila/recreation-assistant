package ru.recreation.recreationassistant.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.models.Event;
import ru.recreation.recreationassistant.services.SearchEventService;
import ru.recreation.recreationassistant.models.Exhibition;

import java.util.List;

@Service
@Slf4j
public class SearchEventServiceImpl implements SearchEventService {
    private static final String URL = "https://kudago.com/public-api/v1.4/places/";

    private final RestTemplateWork restTemplateWork;

    public SearchEventServiceImpl(RestTemplateWork restTemplateWork) {
        this.restTemplateWork = restTemplateWork;
    }

    public List<Event> getRecommendation(User user, String category) {
        try {
            log.info("SearchEventService getRecommendation method start");
            MultiValueMap<String, String> map = restTemplateWork.getParams(user, category);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplateWork.getResponse(URL, map, request);
            log.info("Creating exhibition from respone body");
            Exhibition exhibition = restTemplateWork.getJacksonResult(response, Exhibition.class);
            return exhibition.results;
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }
}