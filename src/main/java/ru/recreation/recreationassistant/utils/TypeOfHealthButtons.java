package ru.recreation.recreationassistant.utils;

public enum TypeOfHealthButtons implements ButtonInfo {
    BUTTON_ALCOHOL("Алкогольный коктейль", "Alcohol-Cocktail"),
    BUTTON_FREE_ALCOHOL("Безалкогольный коктейль", "Alcohol-Free"),
    BUTTON_VEGETARIAN("Вегатарианское", "Vegetarian");

    TypeOfHealthButtons(String text, String id) {
        buttonText = text;
        buttonId = id;
    }

    private final String buttonText;
    private final String buttonId;

    @Override
    public String getButtonText() {
        return buttonText;
    }

    @Override
    public String getButtonId() {
        return buttonId;
    }
}
