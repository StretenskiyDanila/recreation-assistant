package ru.recreation.recreationassistant.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    public int id;
    public String title;
    public String site_url;
    public String address;
    public String phone;
}
