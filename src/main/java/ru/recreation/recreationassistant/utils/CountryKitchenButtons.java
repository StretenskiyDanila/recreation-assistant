package ru.recreation.recreationassistant.utils;

public enum CountryKitchenButtons implements ButtonInfo {
    BUTTON_USA("Американская", "USA"),
    BUTTON_CHN("Китайская", "CHINA"),
    BUTTON_FRZ("Французская", "France"),
    BUTTON_ITA("Итальянская", "Italy"),
    BUTTON_ASIA("Азиатская", "ASIA"),
    BUTTON_WORLD("Мировая", "WORLD");

    CountryKitchenButtons(String text, String id) {
        buttonText = text;
        buttonId = id;
    }

    private final String buttonText;
    private final String buttonId;

    @Override
    public String getButtonText() {
        return null;
    }

    @Override
    public String getButtonId() {
        return null;
    }
}
