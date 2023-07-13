package ru.recreation.recreationassistant.utils;

public enum StartButtons implements ButtonInfo {
    BUTTON_HOME("Сидеть дома", "HOME"),
    BUTTON_STREET("Идти гулять", "STREET");

    StartButtons(String text, String id) {
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
