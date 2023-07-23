package ru.recreation.recreationassistant.utils;

import lombok.Getter;

@Getter
public enum TypeOfHealthButtons implements ButtonInfo {

    BUTTON_FREE_ALCOHOL("Без алкоголя", "alcohol-free"),
    BUTTON_VEGETARIAN("Вегетарианская пища", "vegetarian"),
    BUTTON_SKIP("Пропустить", "SKIP");


    TypeOfHealthButtons(String text, String id) {
        buttonText = text;
        buttonId = id;
    }

    private final String buttonText;
    private final String buttonId;

}
