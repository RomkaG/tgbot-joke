package com.rg.tgjoke.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rg.tgjoke.config.BotConfig;
import com.rg.tgjoke.model.Joke;
import com.rg.tgjoke.model.JokeRepository;
import com.rg.tgjoke.model.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component  //аннотация, которая позволяет спринг автоматически обнаруживать пользовательские бин-компоненты
public class TelegramBot extends TelegramLongPollingBot {

    //конфигурацция бота
    @Autowired  //аннотация используется для автоматического связывания компонентов бина между собой
    final BotConfig config;

    //описание команд в хелпе
    static final String HELP_TEXT = "Бот создан для отправки случайной шутки из базы данных каждый раз при запросе.\n\n" +
            "Вы можете выполнять команды из главного меню слева или вводя команды вручную\n\n" +
            "Команда /start вызов welcome-сообщения\n\n" +
            "Команда /joke отправка случайной шутки\n\n" +
            "Команда /settings доступные параметры для настройки\n\n" +
            "Команда /help просмотр этого сообщения снова\n";

    //сущность для работы с таблицей User
    @Autowired
    private UserRepository userRepository;

    //сущность для работы с таблицей Joke
    @Autowired
    private JokeRepository jokeRepository;

    static final int MAX_JOKE_ID = 3772; //3773 - 1

    //конструктор бота
    public TelegramBot(BotConfig config) {
        this.config = config;

        //список команд бота
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "отправка welcome-сообщения"));  //старт бота
        listOfCommands.add(new BotCommand("/joke", "отправка случайной шутки"));  //случайная шутка
        listOfCommands.add(new BotCommand("/help", "информация как юзать данного бота"));   //помощь
        listOfCommands.add(new BotCommand("/settings", "установка настроек юзера"));    //настройки профиля юзера

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    //получаем юзернейм бота
    @Override
    public String getBotUsername() {
        return config.getBotUserName();
    }

    //получаем токен бота
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    //данный метод вызывается каждый раз, когда пользователь отправляет в бот сообщение
    // данный метод обрабатывает поступающие от пользователя команды
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        //Проверка наличия в Update сообщения + проверка наличия в сообщении текста
        if (update.hasMessage() && update.getMessage().hasText()) {
            //Получение текста сообщения в переменную
            String messageText = update.getMessage().getText();
            //Получение id чата в переменную
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> {
                    showStart(chatId, update.getMessage().getChat().getFirstName());
                    try {
                        ObjectMapper objectMapper = new ObjectMapper(); //маппинг для объектов
                        TypeFactory typeFactory = objectMapper.getTypeFactory();
                        List <Joke> jokeList = objectMapper.readValue(new File("db/stupidstuff.json"),  //сохранение объектов типа joke
                                TypeFactory.defaultInstance().constructCollectionType(List.class, Joke.class)); //из json в list
                        jokeRepository.saveAll(jokeList);   //сохранение шуток из list в таблицу БД
                    } catch (IOException e) {
                        log.error(Arrays.toString(e.getStackTrace()));
                    }
                }
                case "/joke" -> {
                    var r = new Random();
                    var number = r.nextInt(MAX_JOKE_ID) +1;
                    jokeRepository.findById(number);

                }
                default -> commandNotFound(chatId);
            }

        }
    }


    //приветствие юзера
    private void showStart(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode(
                "Хай, " + name + "! :smile:" + " Я простой бот по отправке случайных шуток. \n");
        sendMessage(answer, chatId);
    }

    //команда не распознана
    private void commandNotFound(long chatId) {

        String answer = EmojiParser.parseToUnicode(
                "Команда не распознана, пожалуйста, проверьте и повторите попытку :stuck_out_tongue_winking_eye: ");
        sendMessage(answer, chatId);
    }

    //метод формирования сообщения для отправки юзеру
    private void sendMessage(String textToSend, long chatId) {
        SendMessage message = new SendMessage();    //создание объекта для сообщения
        message.setChatId(String.valueOf(chatId));  //установка id чата, в который отправится сообщение
        message.setText(textToSend);                //передача текста сообщения в объект сообщения
        try {
            execute(message);   //Отправка объекта сообщения юзеру
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
}

