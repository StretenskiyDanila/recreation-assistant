package ru.recreation.recreationassistant.utils;

import lombok.Getter;

@Getter
public enum CountryKitchenButtons implements ButtonInfo {
    BUTTON_USA("Американская", "american"),
    BUTTON_CHN("Китайская", "chinese"),
    BUTTON_FRZ("Французская", "french"),
    BUTTON_ITA("Итальянская", "italian"),
    BUTTON_ASIA("Азиатская", "asian"),
    BUTTON_WORLD("Мировая", "world"),
    BUTTON_SKIP("Пропустить", "SKIP");


    CountryKitchenButtons(String text, String id) {
        buttonText = text;
        buttonId = id;
    }

    private final String buttonText;
    private final String buttonId;

}
