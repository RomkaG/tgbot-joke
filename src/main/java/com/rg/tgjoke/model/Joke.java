package com.rg.tgjoke.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity //аннотация указывает, что данный класс является сущностью (таблицей)
@Data
public class Joke {

    @Column(length = 2550000)   //аннотация указывает явное поведение столбца
                                //указываем макс длину столбца
    private String body;        //тело шутки

    private String category;    //категория

    @Id //аннотация указывает первичный ключ таблицы
    private Integer id; //номер шутки

    private double rating;  //рейтинг шутки
}
