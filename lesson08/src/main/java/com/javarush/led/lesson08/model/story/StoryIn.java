package com.javarush.led.lesson08.model.story;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryIn {
    @Positive
    Long id;

    @Positive
    Long editorId;

    @NotBlank
    @Size(min = 2, max = 64)
    String title;

    @Size(min = 4, max = 2048)
    String content;

    LocalDateTime created;
    LocalDateTime modified;

}
