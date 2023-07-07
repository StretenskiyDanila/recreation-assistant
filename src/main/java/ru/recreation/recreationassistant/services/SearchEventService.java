package ru.recreation.recreationassistant.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.recreation.recreationassistant.models.Result;

import java.util.List;

public interface SearchEventService {
    List<Result> makingRequest(String location, String category) throws JsonProcessingException;
}
