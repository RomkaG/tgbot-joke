package com.rg.tgjoke.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

    @Configuration  //аннотация на уровне класса, указывающая на то,
                    //что объект является источником определений бина.
                    //Классы, аннотированные @Configuration, объявляют бины через методы, аннотированные @Bean

    @Data           //сокращенная аннотация,
                    // сочетающая возможности @ToString , @EqualsAndHashCode , @Getter @Setter и @RequiredArgsConstructor

    @PropertySource("application.properties") //аннотация регистрирует в спринг-проге property файл
    public class BotConfig {

        @Value("${telegram.botName}")   //аннотация для "инъекции" нужного значения из property
        String botUserName;

        @Value("${telegram.botToken}")
        String token;

    }
