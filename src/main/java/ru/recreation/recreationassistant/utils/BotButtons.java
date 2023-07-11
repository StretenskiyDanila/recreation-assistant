package ru.recreation.recreationassistant.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BotButtons {

    public static void startChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        createButtons("Чем Вы планируете заняться?", StartButtons.class, bot, chatId);
    }

    public static void cityChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        createButtons("Выберите город:", CityButtons.class, bot, chatId);
    }

    public static void eventChoise(long chatId, TelegramLongPollingBot bot) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        createButtons("Выберите мероприятие:", EventButtons.class, bot, chatId);
    }

    private static void addButton(InlineKeyboardButton button, String buttonText, final String buttonId) {
        button.setText(buttonText);
        button.setCallbackData(buttonId);
    }

    private static <T extends Enum<T> & ButtonInfo> void createButtons(String textMessage, Class<T> enumClass, TelegramLongPollingBot bot, long chatId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textMessage);
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        Method valuesMethod = enumClass.getMethod("values");
        T[] enumValues = (T[]) valuesMethod.invoke(null);
        for (int i = 0; i < enumValues.length; i++) {
            var button = new InlineKeyboardButton();
            addButton(button, enumValues[i].getButtonText(), enumValues[i].getButtonId());
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
        message.setReplyMarkup(markupInLine);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}