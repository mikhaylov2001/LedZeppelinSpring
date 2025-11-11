package com.javarush.led.lesson08.model.note;

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
public class NoteIn {
    @Positive
    Long id;
    @Positive
    Long storyId;
    @Size(min = 2, max = 2048)
    String content;
}
