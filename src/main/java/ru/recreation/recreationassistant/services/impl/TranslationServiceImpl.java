package ru.recreation.recreationassistant.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.recreation.recreationassistant.services.TranslationService;

@Service
public class TranslationServiceImpl implements TranslationService {

    private static final String URL = "https://translate.googleapis.com/translate_a/single";

    @Override
    public String translate(String line) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client", "gtx");
        map.add("sl", "en");
        map.add("tl", "ru");
        map.add("dt", "t");
        map.add("q", line);

        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL).queryParams(map);
        UriComponents components = builder.build().encode();
        ResponseEntity<String> response = restTemplate.getForEntity(components.toUri(), String.class);

        return parseResponse(response);
    }

    private String parseResponse(ResponseEntity<String> response) {
        Gson gson = new Gson();
        JsonArray jsonObject = gson.fromJson(response.getBody(), JsonArray.class);
        return jsonObject.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsString();
    }
}
