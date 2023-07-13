package ru.recreation.recreationassistant.utils;

public enum TypeOfHealthButtons implements ButtonInfo {
    BUTTON_ALCOHOL("С алкоголем", "alcohol-cocktail"),
    BUTTON_FREE_ALCOHOL("Без алкоголя", "alcohol-free"),
    BUTTON_VEGETARIAN("Вегетарианская пища", "vegetarian");

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
