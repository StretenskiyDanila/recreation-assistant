package ru.recreation.recreationassistant.utils;

public enum CityButtons implements ButtonInfo {
    BUTTON_MSK("Москва","msk"),
    BUTTON_SPB("Санкт-Петербург","spb"),
    BUTTON_NVS("Новосибирск","nsk"),
    BUTTON_NN_("Нижний-Новгород","nnv"),
    BUTTON_KZN("Казань", "kzn"),
    BUTTON_VBR("Выборг", "vbg"),
    BUTTON_SMR("Самара", "smr"),
    BUTTON_KRS("Краснодар", "krd"),
    BUTTON_SCH("Сочи", "sochi"),
    BUTTON_UFA("Уфа", "ufa"),
    BUTTON_KRSNS("Красноярск","krasnoyarsk"),
    BUTTON_EKB("Екатеринбург","ekb");

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
