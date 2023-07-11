package ru.recreation.recreationassistant;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.recreation.recreationassistant.configurations.BotConfig;
import ru.recreation.recreationassistant.utils.BotButtons;
import ru.recreation.recreationassistant.utils.TelegramChatUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    private final String HELP_MESSAGE = "";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Приветственное сообщение"));
        listOfCommands.add(new BotCommand("/help", "Справка"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            final String name = update.getMessage().getChat().getUserName();
            switch (message) {
                case "/start":
                    try {
                        TelegramChatUtils.sendMessage(this, update, "Привет, " + name + '!');
                        BotButtons.startChoise(chatId, this);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                case "/help":
                    try {
                        TelegramChatUtils.sendMessage(this, update, HELP_MESSAGE);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try {
                        TelegramChatUtils.sendMessage(this, update, "Пожалуйста, введите команду");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (callbackData) {
                case "HOME":
                    break;
                case "STREET":
                    try {
                        BotButtons.cityChoise(chatId, this);
                        BotButtons.eventChoise(chatId, this);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                case "ALL":
                    break;

            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
