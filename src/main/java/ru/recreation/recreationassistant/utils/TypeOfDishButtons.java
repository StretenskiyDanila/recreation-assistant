package ru.recreation.recreationassistant.utils;

public enum TypeOfDishButtons implements ButtonInfo {
    BUTTON_BREAKFAST("Завтрак", "BREAKFAST"),
    BUTTON_BRUNCH("Поздний завтрак", "BRUNCH"),
    BUTTON_DINNER("Обед", "DINNER"),
    BUTTON_SUPPER("Ужин", "SUPPER"),
    BUTTON_SNACK("Перекус", "SNACK"),
    BUTTON_TEA_PARTY("Чаепитие", "TEA_PARTY");

    TypeOfDishButtons(String text, String id) {
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
