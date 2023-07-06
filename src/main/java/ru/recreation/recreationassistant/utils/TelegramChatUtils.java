package ru.recreation.recreationassistant.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@UtilityClass
public class TelegramChatUtils {

    public void sendMessage(TelegramLongPollingBot bot, Update update, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(text);
        bot.execute(sendMessage);
    }

}
