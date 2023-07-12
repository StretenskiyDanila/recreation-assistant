package ru.recreation.recreationassistant.utils;

public enum EventButtons implements ButtonInfo {
    BUTTON_CLUB("Клубы","CLUB"),
    BUTTON_PARK("Парки","PARK"),
    BUTTON_QUESTS("Квесты", "QUESTS"),
    BUTTON_RESTAURANTS("Рестораны","RESTAURANTS"),
    BUTTON_MUSEUMS("Музеи и галереи", "MUSEUMS"),
    BUTTON_CONCERTS("Концертные залы", "CONCERTS"),
    BUTTON_HOUSE_OF_CULTURE("Дома культуры", "HOUSE_OF_CULTURE"),
    BUTTON_MOVIES("Кинотеатры", "MOVIES"),
    BUTTON_ATTRACTIONS("Достопремичательности", "ATTRACTIONS");

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
