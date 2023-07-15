package ru.recreation.recreationassistant.utils;

public enum TypeOfMealButtons implements ButtonInfo {
    BUTTON_BREAKFAST("Завтрак", "breakfast"),
    BUTTON_BRUNCH("Поздний завтрак", "brunch"),
    BUTTON_DINNER("Обед", "lunch"),
    BUTTON_SUPPER("Ужин", "dinner"),
    BUTTON_SNACK("Перекус", "snack"),
    BUTTON_TEA_PARTY("Чаепитие", "teatime"),
    BUTTON_SKIP("Пропустить", "SKIP");


    TypeOfMealButtons(String text, String id) {
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
