package ru.recreation.recreationassistant.utils;

public enum TypeOfDishesButtons implements ButtonInfo {
    BUTTON_COCKTAIL("Коктейль", "alcohol cocktail"),
    BUTTON_DESSERTS("Десерт", "desserts"),
    BUTTON_DRINKS("Напиток", "drinks"),
    BUTTON_SALAD("Салат", "salad"),
    BUTTON_SOUP("Суп", "soup"),
    BUTTON_SWEETS("Сладость", "sweets");


    TypeOfDishesButtons(String text, String id) {
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
