package com.rg.tgjoke.config;

import com.rg.tgjoke.service.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//регистрация бота
@Component
@Slf4j  //аннотация для добавления логгера в класс
@AllArgsConstructor //аннотация генерирует конструктор с одним параметром для каждого поля в классе
public class BotInitializer {

    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})   //аннотация для прослушивания события в контексте спринга
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot((LongPollingBot) bot);
        } catch (TelegramApiRequestException e) {
            log.error(e.getMessage());
        }
    }
}
