package com.rg.tgjoke.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "usersDataTable") // создание сущности таблицы с названием "usersDataTable"
@Data
public class User {

    @Id
    private Long chatId;    //номер чата
    private Boolean embedeJoke; //способ получения шутки (новое сообщение/старое обновленное сообщение)
    private String phoneNumber; //номер телефона юзера
    private java.sql.Timestamp registeredAt;    //дата-время регистрации юзера
    private String firstName;   //имя юзера
    private String lastName;    //фамилия юзера
    private String userName; //юзернейм
    private Double latitude;    //расположение юзера - широта
    private Double longitude;   //расположение юзера - долгота
    private String bio; //био юзера
    private String description; //описание
    private String pinnedMessage;   //закрепленное сообщение юзера
}
