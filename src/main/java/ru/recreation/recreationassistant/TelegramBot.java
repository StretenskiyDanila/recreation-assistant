package ru.recreation.recreationassistant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.recreation.recreationassistant.configurations.BotConfig;
import ru.recreation.recreationassistant.entity.User;
import ru.recreation.recreationassistant.models.Recipe;
import ru.recreation.recreationassistant.repositories.UserRepository;
import ru.recreation.recreationassistant.services.RecipeRecommendationsService;
import ru.recreation.recreationassistant.utils.TelegramChatUtils;
import ru.recreation.recreationassistant.utils.BotButtons;
import ru.recreation.recreationassistant.utils.TelegramChatUtils;
import ru.recreation.recreationassistant.utils.StationarySurveyStreet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UserRepository userRepository;
    private final RecipeRecommendationsService recipeRecommendationsService;

    private final String HELP_MESSAGE = "";

    private StationarySurveyStreet currentState;

    public TelegramBot(BotConfig config, UserRepository userRepository, RecipeRecommendationsService recipeRecommendationsService) {
        this.config = config;
        this.userRepository = userRepository;
        this.recipeRecommendationsService = recipeRecommendationsService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Приветственное сообщение"));
        listOfCommands.add(new BotCommand("/menu", "Начало работы"));
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
            String telegramChatId = String.valueOf(update.getMessage().getChatId());
            if (!userRepository.existsByTelegramChatId(telegramChatId)) {
                String userName = update.getMessage().getFrom().getUserName();
                log.info("Added new user with name {} and id {}", userName, telegramChatId);
                userRepository.save(new User(userName, telegramChatId));
            }
            List<Recipe> recipes = recipeRecommendationsService.getRecipeRecommendations(userRepository.findByTelegramChatId(telegramChatId).get(), "beer");
            long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            final String name = update.getMessage().getChat().getUserName();
            switch (message) {
                case "/start":
                    try {
                        TelegramChatUtils.sendMessage(this, chatId, "Привет, " + name + '!');
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "/menu":
                    try {
                        currentState = StationarySurveyStreet.START_SURVEY;
                        BotButtons.startChoise(chatId, this);
                    } catch (TelegramApiException | InvocationTargetException | NoSuchMethodException |
                             IllegalAccessException e) {
                        e.printStackTrace();
                    }
                case "/help":
                    try {
                        TelegramChatUtils.sendMessage(this, chatId, HELP_MESSAGE);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try {
                        TelegramChatUtils.sendMessage(this, chatId, "Пожалуйста, введите команду");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (currentState) {
                case START_SURVEY:
                    switch (callbackData) {
                        case "HOME":
                            break;
                        case "STREET":
                            try {
                                BotButtons.cityChoise(chatId, this);
                                currentState = StationarySurveyStreet.CITY_CHOISE;
                            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                                     TelegramApiException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "ALL":
                            break;
                    }
                    break;
                case CITY_CHOISE:
                    try {
                        BotButtons.eventChoise(chatId, this);
                        currentState = StationarySurveyStreet.EVENT_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case EVENT_CHOISE:
                    try {
                        TelegramChatUtils.sendMessage(this, chatId, "Опрос завершён, результаты...\n Введите команду /menu для начало работы");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
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
