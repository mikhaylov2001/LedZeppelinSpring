package com.javarush.led.lesson08.model.editor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditorOut {
    Long id;
    String login;
    String firstname;
    String lastname;
}
