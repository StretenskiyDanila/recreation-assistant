package ru.recreation.recreationassistant.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.recreation.recreationassistant.services.TranslationService;

@Service
@Slf4j
public class TranslationServiceImpl implements TranslationService {

    private static final String URL = "https://translate.googleapis.com/translate_a/single";

    @Override
    public String translate(String line, String from, String to) {
        log.info("TranslationService translate method with 3 params start");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client", "gtx");
        map.add("sl", from);
        map.add("tl", to);
        map.add("dt", "t");
        map.add("q", line);
        log.info("Making request ...");
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL).queryParams(map);
        UriComponents components = builder.build().encode();
        ResponseEntity<String> response = restTemplate.getForEntity(components.toUri(), String.class);
        log.info("Parsing response ...");
        return parseResponse(response);
    }

    @Override
    public String translate(String line) {
        log.info("TranslationService translate method with 1 param start");
        return translate(line, "en", "ru");
    }


    private String parseResponse(ResponseEntity<String> response) {
        log.info("TranslationService parseResponse method start");
        Gson gson = new Gson();
        JsonArray jsonObject = gson.fromJson(response.getBody(), JsonArray.class);
        return jsonObject.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsString();
    }
}
