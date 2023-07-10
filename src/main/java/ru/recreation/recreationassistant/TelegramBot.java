package ru.recreation.recreationassistant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.recreation.recreationassistant.configurations.BotConfig;
import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.models.Recipe;
import ru.recreation.recreationassistant.repositories.UserRepository;
import ru.recreation.recreationassistant.services.RecipeRecommendationsService;
import ru.recreation.recreationassistant.utils.TelegramChatUtils;

import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UserRepository userRepository;

    private final RecipeRecommendationsService recipeRecommendationsService;

    public TelegramBot(BotConfig config, UserRepository userRepository, RecipeRecommendationsService recipeRecommendationsService) {
        this.config = config;
        this.userRepository = userRepository;
        this.recipeRecommendationsService = recipeRecommendationsService;
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

            List<Recipe> recipes = recipeRecommendationsService.getRecipeRecommendations(userRepository.findByTelegramChatId(telegramChatId).get(), "beer");
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
