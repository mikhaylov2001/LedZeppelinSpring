package com.javarush.led.lesson04.entity;

import com.javarush.led.lesson04.validator.annotation.MaxSize;
import com.javarush.led.lesson04.validator.annotation.MinSize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MinSize(3)
    @MaxSize(7)
    private String login;

    @MaxSize(10)
    private String password;

    private String role;

}
