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


    private final String HELP_MESSAGE = "help";

    private StationarySurveyStreet currentState;
    private String startChoise = "start";

    private String location;
    private String category;

    public TelegramBot(BotConfig config, UserRepository userRepository, RecipeRecommendationsService recipeRecommendationsService,
                       SearchEventService searchEventService, WeatherHelperService weatherHelperService,
                       RecipientCoordinatesCity recipientCoordinatesCity, HealthRepository healthRepository,
                       DishRepository dishRepository, MealRepository mealRepository, CuisineRepository cuisineRepository, TranslationService translationService) {
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
            String data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            User user = userRepository.findByTelegramChatId(String.valueOf(chatId)).get();
            switch (currentState) {
                case START_SURVEY:
                    switch (data) {
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
                        user.setCity(data);
                        userRepository.saveAndFlush(user);
                        currentState = StationarySurveyStreet.EVENT_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case EVENT_CHOISE:
                    try {
                        category = data;
                        StringBuilder message = new StringBuilder("В вашем городе мы рекомендуем посетить:\n");
                        List<Event> events = searchEventService.getRecommendation(user, category);
                        String location = CityButtons.getNameCityOnId(user.getCity());
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
                        Health health = healthRepository.findByHealthLabel(data);
                        user.getHealthTags().add(health);
                        userRepository.saveAndFlush(user);
                        BotButtons.mealsChoise(chatId, this);
                        currentState = StationarySurveyStreet.MEAL_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case MEAL_CHOISE:
                    try {
                        Meal meal = mealRepository.findByMealLabel(data);
                        user.getMealTags().add(meal);
                        userRepository.saveAndFlush(user);
                        BotButtons.dishesChoise(chatId, this);
                        currentState = StationarySurveyStreet.DISHES_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case DISHES_CHOISE:
                    try {
                        Dish dish = dishRepository.findByDishLabel(data);
                        user.getDishTags().add(dish);
                        userRepository.saveAndFlush(user);
                        BotButtons.countryChoise(chatId, this);
                        currentState = StationarySurveyStreet.COUNTRY_CHOISE;
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case COUNTRY_CHOISE:
                    try {
                        Cuisine cuisine = cuisineRepository.findByCuisineLabel(data);
                        user.getCuisineTags().add(cuisine);
                        userRepository.saveAndFlush(user);
                        if (!startChoise.equals("ALL")) {
                            TelegramChatUtils.sendMessage(this, chatId, "Опрос завершён, результаты...\nВведите команду /menu для нового прохождения опроса");
                            currentState = StationarySurveyStreet.START_SURVEY;
                            List<Recipe> recipeRecommendations = recipeRecommendationsService.getRecipeRecommendations(user, "");
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
                            }

                            user.getCuisineTags().clear();
                            user.getMealTags().clear();
                            user.getHealthTags().clear();
                            user.getDishTags().clear();

                            userRepository.saveAndFlush(user);
                        } else {
                            BotButtons.cityChoise(chatId, this);
                            currentState = StationarySurveyStreet.CITY_CHOISE;
                        }
                    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                             TelegramApiException e) {
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
