package com.javarush.led.lesson12.message.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record NoteRequestTo(
        Long id,

        @Min(1)
        Long storyId,


        @Nullable
        @Size(min = 4, max = 2048)
        String content

) {
}
