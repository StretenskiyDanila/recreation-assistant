package ru.recreation.recreationassistant;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.models.Event;
import ru.recreation.recreationassistant.models.Recipe;
import ru.recreation.recreationassistant.repositories.UserRepository;
import ru.recreation.recreationassistant.services.RecipeRecommendationsService;
import ru.recreation.recreationassistant.services.RecipientCoordinatesCity;
import ru.recreation.recreationassistant.services.SearchEventService;
import ru.recreation.recreationassistant.services.WeatherHelperService;
import ru.recreation.recreationassistant.utils.CityButtons;
import ru.recreation.recreationassistant.utils.TelegramChatUtils;
import ru.recreation.recreationassistant.utils.BotButtons;
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
    private final SearchEventService searchEventService;
    private final WeatherHelperService weatherHelperService;
    private final RecipientCoordinatesCity recipientCoordinatesCity;

    private final String HELP_MESSAGE = "help";

    private StationarySurveyStreet currentState;
    private String startChoise = "start";

    private String location;
    private String category;

    public TelegramBot(BotConfig config, UserRepository userRepository, RecipeRecommendationsService recipeRecommendationsService, SearchEventService searchEventService, WeatherHelperService weatherHelperService, RecipientCoordinatesCity recipientCoordinatesCity) {
        this.config = config;
        this.userRepository = userRepository;
        this.recipeRecommendationsService = recipeRecommendationsService;
        this.searchEventService = searchEventService;
        this.weatherHelperService = weatherHelperService;
        this.recipientCoordinatesCity = recipientCoordinatesCity;
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
                        TelegramChatUtils.sendMessage(this, chatId, "Привет, " + name + "!\n" +
                                "Для началы работы введите команду /menu");
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
                    break;
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
                            try {
                                BotButtons.healthChoise(chatId, this);
                                currentState = StationarySurveyStreet.HEALTH_CHOISE;
                            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                                     TelegramApiException e) {
                                e.printStackTrace();
                            }
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
                            startChoise = "ALL";
                            try {
                                BotButtons.healthChoise(chatId, this);
                                currentState = StationarySurveyStreet.HEALTH_CHOISE;
                            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                                     TelegramApiException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    break;
                case CITY_CHOISE:
                    try {
                        BotButtons.eventChoise(chatId, this);
                        location = update.getCallbackQuery().getData();
                        currentState = StationarySurveyStreet.EVENT_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case EVENT_CHOISE:
                    try {
                        category = update.getCallbackQuery().getData();
                        StringBuilder message = new StringBuilder("В вашем городе мы рекомендуем посетить:\n");
                        List<Event> events = searchEventService.getRecommendation(location, category);
                        City city = recipientCoordinatesCity.getCoordinates(location);
                        String recommendationClothes = weatherHelperService.getRecommendation(city);
                        int i = 1;
                        for (Event event : events) {
                            message.append(i++).append(". Название: ").append(event.title).append("\n");
                            message.append("   Находится по адресу: ").append(event.address).append("\n");
                            message.append("   Подробно можно узнать на сайте: ").append(event.site_url).append("\n");
                            message.append("   Либо по номеру телефона: ").append(event.phone).append("\n\n");
                            TelegramChatUtils.sendMessage(this, chatId, message.toString());
                            message = new StringBuilder();
                        }
                        TelegramChatUtils.sendMessage(this, chatId, recommendationClothes);
                    } catch (TelegramApiException | JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    break;

                case HEALTH_CHOISE:
                    try {
                        BotButtons.mealsChoise(chatId, this);
                        currentState = StationarySurveyStreet.MEAL_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case MEAL_CHOISE:
                    try {
                        BotButtons.dishesChoise(chatId, this);
                        currentState = StationarySurveyStreet.DISHES_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case DISHES_CHOISE:
                    try {
                        BotButtons.countryChoise(chatId, this);
                        currentState = StationarySurveyStreet.COUNTRY_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case COUNTRY_CHOISE:
                    try {
                        if (!startChoise.equals("ALL")) {
                            TelegramChatUtils.sendMessage(this, chatId, "Опрос завершён, результаты...\nВведите команду /menu для нового прохождения опроса");
                            currentState = StationarySurveyStreet.START_SURVEY;
                        } else {
                            BotButtons.cityChoise(chatId, this);
                            currentState = StationarySurveyStreet.CITY_CHOISE;
                        }
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | TelegramApiException e) {
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
