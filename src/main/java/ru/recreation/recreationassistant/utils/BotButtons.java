package ru.recreation.recreationassistant.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BotButtons {

    public void startChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Чем Вы планируете заняться?", StartButtons.class, bot, chatId);
    }

    public void cityChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите город:", CityButtons.class, bot, chatId);
    }

    public void eventChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите мероприятие:", EventButtons.class, bot, chatId);
    }

    public void countryChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите страну кухни:", CountryKitchenButtons.class, bot, chatId);
    }

    public void dishesChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите тип посуды:", TypeOfDishesButtons.class, bot, chatId);
    }

    public void healthChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите тип пищи:", TypeOfHealthButtons.class, bot, chatId);
    }

    public void mealsChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите приём пищи:", TypeOfMealButtons.class, bot, chatId);
    }

    private void addButton(InlineKeyboardButton button, String buttonText, final String buttonId) {
        button.setText(buttonText);
        button.setCallbackData(buttonId);
    }

    private <T extends Enum<T> & ButtonInfo> void createButtons(String textMessage, Class<T> enumClass, TelegramLongPollingBot bot, long chatId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, TelegramApiException {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        Method valuesMethod = enumClass.getMethod("values");
        T[] enumValues = (T[]) valuesMethod.invoke(null);
        for (T enumValue : enumValues) {
            var button = new InlineKeyboardButton();
            addButton(button, enumValue.getButtonText(), enumValue.getButtonId());
            rowInLine.add(button);
            if (rowInLine.size() == 3) {
                rowsInLine.add(rowInLine);
                rowInLine = new ArrayList<>();
            }
        }
        if (!rowInLine.isEmpty()) {
            rowsInLine.add(rowInLine);
        }
        markupInLine.setKeyboard(rowsInLine);
        TelegramChatUtils.sendKeyboardMessage(bot, chatId, textMessage, markupInLine);
    }
}