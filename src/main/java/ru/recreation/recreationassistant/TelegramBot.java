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
import ru.recreation.recreationassistant.entity.*;
import ru.recreation.recreationassistant.models.City;
import ru.recreation.recreationassistant.models.Event;
import ru.recreation.recreationassistant.models.Recipe;
import ru.recreation.recreationassistant.repositories.*;
import ru.recreation.recreationassistant.services.*;
import ru.recreation.recreationassistant.utils.BotButtons;
import ru.recreation.recreationassistant.utils.CityButtons;
import ru.recreation.recreationassistant.utils.StationarySurveyStreet;
import ru.recreation.recreationassistant.utils.TelegramChatUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    private final BotConfig config;
    private final UserRepository userRepository;
    private final RecipeRecommendationsService recipeRecommendationsService;
    private final SearchEventService searchEventService;
    private final WeatherHelperService weatherHelperService;
    private final RecipientCoordinatesCity recipientCoordinatesCity;

    private final HealthRepository healthRepository;
    private final DishRepository dishRepository;
    private final MealRepository mealRepository;
    private final CuisineRepository cuisineRepository;

    private final TranslationService translationService;

    private final UserService userService;


    public TelegramBot(BotConfig config, UserRepository userRepository, RecipeRecommendationsService recipeRecommendationsService,
                       SearchEventService searchEventService, WeatherHelperService weatherHelperService,
                       RecipientCoordinatesCity recipientCoordinatesCity, HealthRepository healthRepository,
                       DishRepository dishRepository, MealRepository mealRepository, CuisineRepository cuisineRepository, TranslationService translationService, UserService userService) {
        this.config = config;
        this.userRepository = userRepository;
        this.recipeRecommendationsService = recipeRecommendationsService;
        this.searchEventService = searchEventService;
        this.weatherHelperService = weatherHelperService;
        this.recipientCoordinatesCity = recipientCoordinatesCity;
        this.healthRepository = healthRepository;
        this.dishRepository = dishRepository;
        this.mealRepository = mealRepository;
        this.cuisineRepository = cuisineRepository;
        this.translationService = translationService;
        this.userService = userService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Приветственное сообщение"));
        listOfCommands.add(new BotCommand("/menu", "Начало работы"));
        listOfCommands.add(new BotCommand("/help", "Справка"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("TelegramApiException occurred");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            User user = new User();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getUserName();
            if (!userRepository.existsByTelegramChatId(String.valueOf(chatId))) {
                userService.save(user, userName, chatId);
                log.info("Added new user with name {} and id {}", userName, chatId);
            } else {
                user = userService.getUser(chatId);
            }
            String message = update.getMessage().getText();
            switch (message) {
                case "/start" -> {
                    log.info("/start command enter");
                    try {
                        TelegramChatUtils.sendMessage(this, chatId, "Привет, " + userName + "!\n" +
                                "Для началы работы введите команду /menu");
                    } catch (TelegramApiException e) {
                        log.error("TelegramApiException occurred");
                    }
                }
                case "/menu" -> {
                    log.info("/menu command enter");
                    try {
                        userService.setCurrentState(user, StationarySurveyStreet.START_SURVEY);
                        BotButtons.startChoise(chatId, this);
                    } catch (TelegramApiException | InvocationTargetException | NoSuchMethodException |
                             IllegalAccessException e) {
                        log.error("Exception occurred");
                    }
                }
                case "/help" -> {
                    log.info("/help command enter");
                    try {
                        StringBuilder HELP_MESSAGE = new StringBuilder("Привет, это бот-помощник по отдыху, который поможет Вам с пользой провести время!\n" +
                                "Для начала необходимо ввести или выбрать в меню команду /menu, Вам нужно будет выбрать, чем вы желаете заняться: пойти гулять или остаться дома\n\n" +
                                "В случае, если Вы решите остаться дома, Вам будет предложено пройти опрос, который сформирует Ваши предпочтения в еде." +
                                " По результатам опроса Вы получите рецепты различных блюд и сможете их повторить.\n" +
                                "\nЕсли же Вы собираетесь идти на улицу, бот подскажет, какая сейчас погода в выбранном городе, а так же отправит Вам афишу мероприятий");
                        TelegramChatUtils.sendMessage(this, chatId, HELP_MESSAGE.toString());
                    } catch (TelegramApiException e) {
                        log.error("TelegramApiException occurred");
                    }
                }
                default -> {
                    try {
                        if (userService.getUser(chatId).getCurrentState().equals(StationarySurveyStreet.END_FOOD_CHOISE.name())) {
                            if (message.equalsIgnoreCase("Пропустить")) {
                                message = "";
                            }
                            List<Recipe> recipeRecommendations = recipeRecommendationsService.getRecipeRecommendations(user, message);
                            StringBuilder recommendation = new StringBuilder("Предлагаемые рецепты по вашим предпочтениям...:\n");
                            if (recipeRecommendations.isEmpty()) {
                                TelegramChatUtils.sendMessage(this, chatId, "К сожалению, мы ничего не нашли :(");
                            }
                            for (Recipe recipe : recipeRecommendations) {
                                recommendation.append("Название блюда: ").append(translationService.translate(recipe.getLabel())).append('\n');
                                recommendation.append("Необходимые ингридиенты: \n");
                                StringBuilder finalRecommendation = recommendation;
                                recipe.getIngredientLines().forEach(s -> finalRecommendation.append(translationService.translate(s)).append('\n'));
                                recommendation.append("Примерное время приготовления: ").append(recipe.getTotalTime()).append(" минут\n");
                                recommendation.append("Калорийность блюда: ").append(decimalFormat.format(Double.parseDouble(recipe.getCalories()))).append("\n");
                                recommendation.append("Кухня: ").append(translationService.translate(recipe.getCuisineType().stream().findFirst().orElse("Не определено"))).append("\n");
                                recommendation.append("Узнать подробнее о рецепте: ").append(recipe.getUrl());
                                TelegramChatUtils.sendMessage(this, chatId, recommendation.toString());
                                recommendation = new StringBuilder("\n");
                                userService.clearUserTags(user);
                            }
                            TelegramChatUtils.sendMessage(this, chatId, "Введите команду /menu для нового прохождения опроса");
                            userService.setCurrentState(user, StationarySurveyStreet.START_SURVEY);
                        } else {
                            TelegramChatUtils.sendMessage(this, chatId, "Пожалуйста, введите команду");
                        }
                    } catch (TelegramApiException e) {
                        log.error("TelegramApiException occurred");
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            User user = userService.getUser(chatId);
            StationarySurveyStreet currentState = StationarySurveyStreet.valueOf(user.getCurrentState());
            switch (currentState) {
                case START_SURVEY -> {
                    switch (data) {
                        case "HOME" -> {
                            log.info("HOME branch in START_SURVEY enter");
                            try {
                                BotButtons.healthChoise(chatId, this);

                                userService.setCurrentState(user, StationarySurveyStreet.HEALTH_CHOISE);
                            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                                     TelegramApiException e) {
                                log.error("Exception occurred");
                            }
                        }
                        case "STREET" -> {
                            log.info("STREET branch in START_SURVEY enter");
                            try {
                                BotButtons.cityChoise(chatId, this);
                                userService.setCurrentState(user, StationarySurveyStreet.CITY_CHOISE);
                            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                                     TelegramApiException e) {
                                log.error("Exception occurred");
                            }
                        }
                    }
                }
                case CITY_CHOISE -> {
                    log.info("CITY_CHOISE branch start");
                    try {
                        BotButtons.eventChoise(chatId, this);
                        userService.setCity(user, data);
                        userService.setCurrentState(user, StationarySurveyStreet.EVENT_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        log.error("Exception occurred");
                    }
                }
                case EVENT_CHOISE -> {
                    log.info("EVENT_CHOISE branch start");
                    try {
                        List<Event> events = searchEventService.getRecommendation(user, data);
                        City city = recipientCoordinatesCity.getCoordinates(CityButtons.getNameCityOnId(user.getCity()));
                        String recommendationClothes = weatherHelperService.getRecommendation(city);
                        StringBuilder message = new StringBuilder("В вашем городе мы рекомендуем посетить:\n");
                        int i = 1;
                        for (Event event : events) {
                            message.append(i++).append(". Название: ").append(event.getTitle()).append("\n");
                            message.append("   Находится по адресу: ").append(event.getAddress()).append("\n");
                            message.append("   Подробно можно узнать на сайте: ").append(event.getSiteUrl()).append("\n");
                            TelegramChatUtils.sendMessage(this, chatId, message.toString());
                            message = new StringBuilder();
                        }
                        TelegramChatUtils.sendMessage(this, chatId, recommendationClothes);
                        TelegramChatUtils.sendMessage(this, chatId, "Введите команду /menu для нового прохождения опроса");
                    } catch (TelegramApiException | JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                case HEALTH_CHOISE -> {
                    log.info("HEALTH_CHOISE branch start");
                    try {
                        if (!data.equals("SKIP")) {
                            Health health = healthRepository.findByHealthLabel(data);
                            userService.addHealthTag(user, health);
                        }
                        BotButtons.mealsChoise(chatId, this);
                        userService.setCurrentState(user, StationarySurveyStreet.MEAL_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        log.error("Exception occurred");
                    }
                }
                case MEAL_CHOISE -> {
                    log.info("MEAL_CHOISE branch start");
                    try {
                        if (!data.equals("SKIP")) {
                            Meal meal = mealRepository.findByMealLabel(data);
                            userService.addMealTag(user, meal);

                        }
                        BotButtons.dishesChoise(chatId, this);
                        userService.setCurrentState(user, StationarySurveyStreet.DISHES_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        log.error("Exception occurred");
                    }
                }
                case DISHES_CHOISE -> {
                    log.info("DISHES_CHOISE branch start");
                    try {
                        if (!data.equals("SKIP")) {
                            Dish dish = dishRepository.findByDishLabel(data);
                            userService.addDishTag(user, dish);
                        }
                        BotButtons.countryChoise(chatId, this);
                        userService.setCurrentState(user, StationarySurveyStreet.COUNTRY_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        log.error("Exception occurred");
                    }
                }
                case COUNTRY_CHOISE -> {
                    log.info("COUNTRY_CHOISE branch enter");
                    try {
                        if (!data.equals("SKIP")) {
                            Cuisine cuisine = cuisineRepository.findByCuisineLabel(data);
                            userService.addCuisineTag(user, cuisine);
                        }
                        TelegramChatUtils.sendMessage(this, chatId, "Введите ваши предпочтения в еде:");
                        userService.setCurrentState(user, StationarySurveyStreet.END_FOOD_CHOISE);
                    } catch (TelegramApiException e) {
                        log.error("TelegramApiException occurred");
                    }
                }
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
