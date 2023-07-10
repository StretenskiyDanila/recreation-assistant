package ru.recreation.recreationassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class RecreationAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecreationAssistantApplication.class, args);
    }
}
