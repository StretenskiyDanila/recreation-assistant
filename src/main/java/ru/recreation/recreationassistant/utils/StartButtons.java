package ru.recreation.recreationassistant.utils;

import lombok.Getter;

@Getter
public enum StartButtons implements ButtonInfo {
    BUTTON_HOME("Сидеть дома", "HOME"),
    BUTTON_STREET("Идти гулять", "STREET");

    StartButtons(String text, String id) {
        buttonText = text;
        buttonId = id;
    }

    private final String buttonText;
    private final String buttonId;

}
