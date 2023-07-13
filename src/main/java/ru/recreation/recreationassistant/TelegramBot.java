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


    private final String HELP_MESSAGE = "help";

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
            e.printStackTrace();
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
                case "/start":
                    try {
                        TelegramChatUtils.sendMessage(this, chatId, "Привет, " + userName + "!\n" +
                                "Для началы работы введите команду /menu");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "/menu":
                    try {
                        userService.setCurrentState(user, StationarySurveyStreet.START_SURVEY);
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
                        if (userService.getUser(chatId).getCurrentState().equals(StationarySurveyStreet.END_FOOD_CHOISE.toString())) {
                            if (!message.equals("Пропустить")) {
                                List<Recipe> recipeRecommendations = recipeRecommendationsService.getRecipeRecommendations(user, message);
                                StringBuilder recommendation = new StringBuilder("Предлагаемые рецепты по вашим предпочтениям...:\n");
                                if (recipeRecommendations.isEmpty()) {
                                    TelegramChatUtils.sendMessage(this, chatId, "К сожалению, мы ничего не нашли :(");
                                }
                                for (Recipe recipe : recipeRecommendations) {
                                    recommendation.append("Название блюда: ").append(translationService.translate(recipe.label)).append('\n');
                                    recommendation.append("Необходимые ингридиенты: \n");
                                    StringBuilder finalRecommendation = recommendation;
                                    recipe.ingredientLines.forEach(s -> finalRecommendation.append(translationService.translate(s)).append('\n'));
                                    recommendation.append("Примерное время приготовления: ").append(recipe.totalTime).append(" минут\n");
                                    recommendation.append("Калорийность блюда: ").append(decimalFormat.format(Double.parseDouble(recipe.calories))).append("\n");
                                    recommendation.append("Кухня: ").append(translationService.translate(recipe.cuisineType.stream().findFirst().orElse("Не определено"))).append("\n");
                                    recommendation.append("Узнать подробнее о рецепте: ").append(recipe.url);
                                    TelegramChatUtils.sendMessage(this, chatId, recommendation.toString());
                                    recommendation = new StringBuilder("\n");
                                    userService.clearUserTags(user);
                                    TelegramChatUtils.sendMessage(this, chatId, "Введите команду /menu для нового прохождения опроса");
                                    userService.setCurrentState(user, StationarySurveyStreet.START_SURVEY);
                                }
                            }
                        } else {
                            TelegramChatUtils.sendMessage(this, chatId, "Пожалуйста, введите команду");
                        }
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            User user = userService.getUser(chatId);
            StationarySurveyStreet currentState = StationarySurveyStreet.valueOf(user.getCurrentState());
            switch (currentState) {
                case START_SURVEY:
                    switch (data) {
                        case "HOME":
                            try {
                                BotButtons.healthChoise(chatId, this);

                                userService.setCurrentState(user, StationarySurveyStreet.HEALTH_CHOISE);
                            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                                     TelegramApiException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "STREET":
                            try {
                                BotButtons.cityChoise(chatId, this);
                                userService.setCurrentState(user, StationarySurveyStreet.CITY_CHOISE);
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
                        userService.setCity(user, data);
                        userService.setCurrentState(user, StationarySurveyStreet.EVENT_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case EVENT_CHOISE:
                    try {
                        List<Event> events = searchEventService.getRecommendation(user, data);
                        City city = recipientCoordinatesCity.getCoordinates(CityButtons.getNameCityOnId(user.getCity()));
                        String recommendationClothes = weatherHelperService.getRecommendation(city);
                        StringBuilder message = new StringBuilder("В вашем городе мы рекомендуем посетить:\n");
                        int i = 1;
                        for (Event event : events) {
                            message.append(i++).append(". Название: ").append(event.title).append("\n");
                            message.append("   Находится по адресу: ").append(event.address).append("\n");
                            message.append("   Подробно можно узнать на сайте: ").append(event.site_url).append("\n");
                            TelegramChatUtils.sendMessage(this, chatId, message.toString());
                            message = new StringBuilder();
                        }
                        TelegramChatUtils.sendMessage(this, chatId, recommendationClothes);
                        TelegramChatUtils.sendMessage(this, chatId, "Введите команду /menu для нового прохождения опроса");
                    } catch (TelegramApiException | JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    break;

                case HEALTH_CHOISE:
                    try {
                        if (!data.equals("SKIP")) {
                            Health health = healthRepository.findByHealthLabel(data);
                            userService.addHealthTag(user, health);
                        }
                        BotButtons.mealsChoise(chatId, this);
                        userService.setCurrentState(user, StationarySurveyStreet.MEAL_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case MEAL_CHOISE:
                    try {
                        if (!data.equals("SKIP")) {
                            Meal meal = mealRepository.findByMealLabel(data);
                            userService.addMealTag(user, meal);

                        }
                        BotButtons.dishesChoise(chatId, this);
                        userService.setCurrentState(user, StationarySurveyStreet.DISHES_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case DISHES_CHOISE:
                    try {
                        if (!data.equals("SKIP")) {
                            Dish dish = dishRepository.findByDishLabel(data);
                            userService.addDishTag(user, dish);
                        }
                        BotButtons.countryChoise(chatId, this);
                        userService.setCurrentState(user, StationarySurveyStreet.COUNTRY_CHOISE);
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case COUNTRY_CHOISE:
                    try {
                        if (!data.equals("SKIP")) {
                            Cuisine cuisine = cuisineRepository.findByCuisineLabel(data);
                            userService.addCuisineTag(user, cuisine);
                        }
                        TelegramChatUtils.sendMessage(this, chatId, "Введите ваши предпочтения в еде:");
                        userService.setCurrentState(user, StationarySurveyStreet.END_FOOD_CHOISE);
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
