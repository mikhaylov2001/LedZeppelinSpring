package com.javarush.led.lesson08.model.note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteOut {
    Long id;
    Long storyId;
    String content;
}
