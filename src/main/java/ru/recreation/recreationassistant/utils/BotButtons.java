package ru.recreation.recreationassistant.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class BotButtons {

    private static final String BUTTON_HOME_TEXT = "Сидеть дома";
    private  static final String BUTTON_STREET_TEXT = "Идти гулять";
    private  static final String BUTTON_ALL_TEXT = "Всё вместе";
    public static final String BUTTON_HOME_ID = "HOME";
    public static final String BUTTON_STREET_ID = "STREET";
    public  static final String BUTTON_ALL_ID = "ALL";

    private  static final String BUTTON_MSK_TEXT = "Москва";
    private  static final String BUTTON_MSK_ID = "MSK";
    private  static final String BUTTON_SPB_TEXT = "Санкт-Петербург";
    private  static final String BUTTON_SPB_ID = "SPB";
    private  static final String BUTTON_NVS_TEXT = "Новосибирск";
    private  static final String BUTTON_NVS_ID = "NVS";
    private  static final String BUTTON_NN_TEXT = "Нижний-Новгород";
    private  static final String BUTTON_NN_ID = "NN";
    private  static final String BUTTON_KZN_TEXT = "Казань";
    private  static final String BUTTON_KZN_ID = "KZN";
    private  static final String BUTTON_VBR_TEXT = "Выборг";
    private  static final String BUTTON_VBR_ID = "VBR";
    private static final String BUTTON_SMR_TEXT = "Самара";
    private static final String BUTTON_SMR_ID = "SMR";
    private static final String BUTTON_KRS_TEXT = "Краснодар";
    private static final String BUTTON_KRS_ID = "KRS";
    private static final String BUTTON_SCH_TEXT = "Сочи";
    private static final String BUTTON_SCH_ID = "SCH";
    private static final String BUTTON_UFA_TEXT = "Уфа";
    private static final String BUTTON_UFA_ID = "UFA";
    private static final String BUTTON_KRSNS_TEXT = "Красноярск";
    private static final String BUTTON_KRSNS_ID = "KRSNS";
    private static final String BUTTON_EKB_TEXT = "Екатеринбург";
    private static final String BUTTON_EKB_ID = "EKB";

    private static final String BUTTON_CLUB_TEXT = "Клубы";
    private static final String BUTTON_CLUB_ID = "CLUB";
    private static final String BUTTON_PARK_TEXT = "Парки";
    private static final String BUTTON_PARK_ID = "PARK";
    private static final String BUTTON_QUESTS_TEXT = "Квесты";
    private static final String BUTTON_QUESTS_ID = "QUESTS";
    private static final String BUTTON_RESTAURANTS_TEXT = "Рестораны";
    private static final String BUTTON_RESTAURANTS_ID = "RESTAURANTS";
    private static final String BUTTON_MUSEUMS_TEXT = "Музеи и галереи";
    private static final String BUTTON_MUSEUMS_ID = "MUSEUMS";
    private static final String BUTTON_CONCERTS_TEXT = "Концертные залы";
    private static final String BUTTON_CONCERTS_ID = "CONCERTS";
    private static final String BUTTON_HOUSE_OF_CULTURE_TEXT = "Дома культуры";
    private static final String BUTTON_HOUSE_OF_CULTURE_ID = "HOUSE_OF_CULTURE";
    private static final String BUTTON_MOVIES_TEXT = "Кинотеатры";
    private static final String BUTTON_MOVIES_ID = "MOVIES";
    private static final String BUTTON_ATTRACTIONS_TEXT = "Достопремичательности";
    private static final String BUTTON_ATTRACTIONS_ID = "ATTRACTIONS";

    public static void startChoise(long chatId, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Чем вы планируете заняться?");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var homeButton = new InlineKeyboardButton();
        addButton(homeButton, BUTTON_HOME_TEXT, BUTTON_HOME_ID);
        var streetButton = new InlineKeyboardButton();
        addButton(streetButton, BUTTON_STREET_TEXT, BUTTON_STREET_ID);
        var allButton = new InlineKeyboardButton();
        addButton(allButton, BUTTON_ALL_TEXT, BUTTON_ALL_ID);
        rowInLine.add(homeButton);
        rowInLine.add(streetButton);
        rowInLine.add(allButton);
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public static void cityChoise(long chatId, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите город:");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        List<InlineKeyboardButton> fourthRow = new ArrayList<>();
        var mskButton = new InlineKeyboardButton();
        addButton(mskButton, BUTTON_MSK_TEXT, BUTTON_MSK_ID);
        var spbButton = new InlineKeyboardButton();
        addButton(spbButton, BUTTON_SPB_TEXT, BUTTON_SPB_ID);
        var nvsButton = new InlineKeyboardButton();
        addButton(nvsButton, BUTTON_NVS_TEXT, BUTTON_NVS_ID);
        var nnButton = new InlineKeyboardButton();
        addButton(nnButton, BUTTON_NN_TEXT, BUTTON_NN_ID);
        var kznButton = new InlineKeyboardButton();
        addButton(kznButton, BUTTON_KZN_TEXT, BUTTON_KZN_ID);
        var vbrButton = new InlineKeyboardButton();
        addButton(vbrButton, BUTTON_VBR_TEXT, BUTTON_VBR_ID);
        var smrButton = new InlineKeyboardButton();
        addButton(smrButton, BUTTON_SMR_TEXT, BUTTON_SMR_ID);
        var krsButton = new InlineKeyboardButton();
        addButton(krsButton, BUTTON_KRS_TEXT, BUTTON_KRS_ID);
        var schButton = new InlineKeyboardButton();
        addButton(schButton, BUTTON_SCH_TEXT, BUTTON_SCH_ID);
        var ufaButton = new InlineKeyboardButton();
        addButton(ufaButton, BUTTON_UFA_TEXT, BUTTON_UFA_ID);
        var krsnsButton = new InlineKeyboardButton();
        addButton(krsnsButton, BUTTON_KRSNS_TEXT, BUTTON_KRSNS_ID);
        var ekbButton = new InlineKeyboardButton();
        addButton(ekbButton, BUTTON_EKB_TEXT, BUTTON_EKB_ID);
        firstRow.add(mskButton);
        firstRow.add(spbButton);
        firstRow.add(nvsButton);
        secondRow.add(nnButton);
        secondRow.add(kznButton);
        secondRow.add(vbrButton);
        thirdRow.add(smrButton);
        thirdRow.add(krsButton);
        thirdRow.add(krsnsButton);
        fourthRow.add(ekbButton);
        fourthRow.add(ufaButton);
        fourthRow.add(schButton);
        rowsInLine.add(firstRow);
        rowsInLine.add(secondRow);
        rowsInLine.add(thirdRow);
        rowsInLine.add(fourthRow);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void eventChoise(long chatId, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        message.setText("Выберите категорию мероприятий:");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();

        var clubButton = new InlineKeyboardButton();
        addButton(clubButton, BUTTON_CLUB_TEXT, BUTTON_CLUB_ID);
        var parkButton = new InlineKeyboardButton();
        addButton(parkButton, BUTTON_PARK_TEXT, BUTTON_PARK_ID);
        var questsButton = new InlineKeyboardButton();
        addButton(questsButton, BUTTON_QUESTS_TEXT, BUTTON_QUESTS_ID);
        var restaurantsButton = new InlineKeyboardButton();
        addButton(restaurantsButton, BUTTON_RESTAURANTS_TEXT, BUTTON_RESTAURANTS_ID);
        var museumsButton = new InlineKeyboardButton();
        addButton(museumsButton, BUTTON_MUSEUMS_TEXT, BUTTON_MUSEUMS_ID);
        var concertsButton = new InlineKeyboardButton();
        addButton(concertsButton, BUTTON_CONCERTS_TEXT, BUTTON_CONCERTS_ID);
        var houseOfCultureButton = new InlineKeyboardButton();
        addButton(houseOfCultureButton, BUTTON_HOUSE_OF_CULTURE_TEXT, BUTTON_HOUSE_OF_CULTURE_ID);
        var moviesButton = new InlineKeyboardButton();
        addButton(moviesButton, BUTTON_MOVIES_TEXT, BUTTON_MOVIES_ID);
        var attractionsButton = new InlineKeyboardButton();
        addButton(attractionsButton, BUTTON_ATTRACTIONS_TEXT, BUTTON_ATTRACTIONS_ID);

        firstRow.add(clubButton);
        firstRow.add(parkButton);
        firstRow.add(questsButton);
        secondRow.add(restaurantsButton);
        secondRow.add(museumsButton);
        secondRow.add(concertsButton);
        thirdRow.add(houseOfCultureButton);
        thirdRow.add(moviesButton);
        thirdRow.add(attractionsButton);

        rowsInLine.add(firstRow);
        rowsInLine.add(secondRow);
        rowsInLine.add(thirdRow);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static void addButton(InlineKeyboardButton button, String buttonText, final String buttonId) {
        button.setText(buttonText);
        button.setCallbackData(buttonId);
    }
}
