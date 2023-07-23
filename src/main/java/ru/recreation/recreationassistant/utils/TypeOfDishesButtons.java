package ru.recreation.recreationassistant.utils;

import lombok.Getter;

@Getter
public enum TypeOfDishesButtons implements ButtonInfo {
    BUTTON_COCKTAIL("Напитки", "drinks"),
    BUTTON_DESSERTS("Десерт", "desserts"),
    BUTTON_DRINKS("Основное блюдо", "main course"),
    BUTTON_SALAD("Салат", "salad"),
    BUTTON_SOUP("Суп", "soup"),
    BUTTON_SWEETS("Сладость", "sweets"),
    BUTTON_ALCOHOL_COCKTAIL("Коктейли", "alcohol cocktail"),
    BUTTON_SANDWICHES("Сэндвичи", "sandwiches"),
    BUTTON_SEAFOOD("Морепродукты", "seafood");


    TypeOfDishesButtons(String text, String id) {
        buttonText = text;
        buttonId = id;
    }

    private final String buttonText;
    private final String buttonId;

}
