package ru.recreation.recreationassistant.utils;

public enum CityButtons implements ButtonInfo {
    BUTTON_MSK("Москва","MSK"),
    BUTTON_SPB("Санкт-Петербург","SPB"),
    BUTTON_NVS("Новосибирск","NVS"),
    BUTTON_NN_("Нижний-Новгород","NN"),
    BUTTON_KZN("Казань", "KZN"),
    BUTTON_VBR("Выборг", "VBR"),
    BUTTON_SMR("Самара", "SMR"),
    BUTTON_KRS("Краснодар", "KRS"),
    BUTTON_SCH("Сочи", "SCH"),
    BUTTON_UFA("Уфа", "UFA"),
    BUTTON_KRSNS("Красноярск","KRSNS"),
    BUTTON_EKB("Екатеринбург","EKB");

    CityButtons(String text, String id) {
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
