package com.javarush.led.lesson08.model.story;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryOut {
    Long id;
    Long editorId;
    String title;
    String content;
    LocalDateTime created;
    LocalDateTime modified;
}
