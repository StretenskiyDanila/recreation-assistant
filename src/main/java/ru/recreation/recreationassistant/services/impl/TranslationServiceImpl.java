package ru.recreation.recreationassistant.services.impl;

import com.google.gson.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.recreation.recreationassistant.services.TranslationService;

@Service
@Slf4j
public class TranslationServiceImpl implements TranslationService {

    private static final String URL = "https://translate.googleapis.com/translate_a/single";

    private final RestTemplateWork restTemplateWork;

    public TranslationServiceImpl(RestTemplateWork restTemplateWork) {
        this.restTemplateWork = restTemplateWork;
    }

    @Override
    public String translate(String line, String from, String to) {
        log.info("TranslationService translate method with 3 params start");
        MultiValueMap<String, String> map = restTemplateWork.getParams(from, to, line);
        ResponseEntity<String> response = restTemplateWork.getResponse(URL, map, null);
        log.info("Parsing response ...");
        return parseResponse(response);
    }

    @Override
    public String translate(String line) {
        log.info("TranslationService translate method with 1 param start");
        return translate(line, "en", "ru");
    }

    private String parseResponse(ResponseEntity<String> response) {
        JsonArray jsonObject = restTemplateWork.getGsonResult(response, JsonArray.class);
        return jsonObject.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsString();
    }

}
