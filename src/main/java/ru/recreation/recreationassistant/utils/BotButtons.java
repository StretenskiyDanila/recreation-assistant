package ru.recreation.recreationassistant.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BotButtons {

    public static void startChoice(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Чем Вы планируете заняться?", StartButtons.class, bot, chatId);
    }

    public static void cityChoice(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите город:", CityButtons.class, bot, chatId);
    }

    public static void eventChoice(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите мероприятие:", EventButtons.class, bot, chatId);
    }

    public static void countryChoice(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите страну кухни:", CountryKitchenButtons.class, bot, chatId);
    }

    public static void dishesChoice(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите тип посуды:", TypeOfDishesButtons.class, bot, chatId);
    }

    public static void healthChoice(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите тип пищи:", TypeOfHealthButtons.class, bot, chatId);
    }

    public static void mealsChoice(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, TelegramApiException {
        createButtons("Выберите приём пищи:", TypeOfMealButtons.class, bot, chatId);
    }

    private static void addButton(InlineKeyboardButton button, String buttonText, final String buttonId) {
        button.setText(buttonText);
        button.setCallbackData(buttonId);
    }

    private static <T extends Enum<T> & ButtonInfo> void createButtons(String textMessage, Class<T> enumClass, TelegramLongPollingBot bot, long chatId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, TelegramApiException {
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