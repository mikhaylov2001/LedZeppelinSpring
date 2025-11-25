package com.javarush.led.lesson12.message.model;

public record NoteResponseTo(
        Long id,
        Long storyId,
        String content
) {
}
