package ru.recreation.recreationassistant.utils;

public enum EventButtons implements ButtonInfo {
    BUTTON_CLUB("Клубы","clubs"),
    BUTTON_PARK("Парки","park"),
    BUTTON_QUESTS("Квесты", "questroomТ"),
    BUTTON_RESTAURANTS("Рестораны","restaurants"),
    BUTTON_MUSEUMS("Музеи и галереи", "museums"),
    BUTTON_CONCERTS("Концертные залы", "concert-hall"),
    BUTTON_HOUSE_OF_CULTURE("Дома культуры", "culture"),
    BUTTON_MOVIES("Кинотеатры", "cinema"),
    BUTTON_ATTRACTIONS("Достопримечательности", "attractions");

    EventButtons(String text, String id) {
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
