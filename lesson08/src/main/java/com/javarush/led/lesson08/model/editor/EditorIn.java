package com.javarush.led.lesson08.model.editor;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditorIn {
    @Positive
    Long id;
    @Size(min = 2, max = 64)
    String login;
    @Size(min = 8, max = 128)
    String password;
    @Size(min = 2, max = 64)
    String firstname;
    @Size(min = 2, max = 64)
    String lastname;
}
