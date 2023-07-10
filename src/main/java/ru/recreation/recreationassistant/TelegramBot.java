package ru.recreation.recreationassistant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.recreation.recreationassistant.configurations.BotConfig;
import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.repositories.UserRepository;
import ru.recreation.recreationassistant.utils.TelegramChatUtils;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UserRepository userRepository;

    public TelegramBot(BotConfig config, UserRepository userRepository) {
        this.config = config;
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            TelegramChatUtils.sendMessage(this, update, "Hi!");
            String telegramChatId = String.valueOf(update.getMessage().getChatId());
            if (!userRepository.existsByTelegramChatId(telegramChatId)) {
                String userName = update.getMessage().getFrom().getUserName();
                log.info("Added new user with name {} and id {}", userName, telegramChatId);
                userRepository.save(new User(userName, telegramChatId));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
