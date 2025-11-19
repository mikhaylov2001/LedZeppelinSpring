package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.story.Story;
import com.javarush.led.lesson10.model.story.StoryIn;
import com.javarush.led.lesson10.model.story.StoryOut;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@RequiredArgsConstructor
public class StoryServiceIT {

    private final StoryService storyService;
    private StoryIn storyIn;
    private final Long TEST_EDITOR_ID = 42L;

    @BeforeEach
    void setUp() {
        // Данные инициализируются в setUp, как в прототипе EditorServiceIT
        storyIn = StoryIn.builder()
                .editorId(TEST_EDITOR_ID)
                .title("Initial Test Story Title")
                .content("A long and detailed content for the initial story.")
                .build();
    }

    private static Stream<Arguments> creationTestData() {
        // Кейс 1: Стандартное создание
        StoryIn input1 = StoryIn.builder()
                .editorId(10L)
                .title("The First Chapter")
                .content("It was a dark and stormy night...")
                .build();
        // Story entity (для соответствия прототипу Arguments.of(In, Entity, Out))
        Story entity1 = Story.builder()
                .id(1L)
                .editorId(10L)
                .title("The First Chapter")
                .content("It was a dark and stormy night...")
                .build();
        StoryOut output1 = StoryOut.builder()
                .editorId(10L)
                .title("The First Chapter")
                .content("It was a dark and stormy night...")
                .build();

        // Кейс 2: Короткие, но валидные данные
        StoryIn input2 = StoryIn.builder()
                .editorId(20L)
                .title("Min Title")
                .content("Min Content.")
                .build();
        Story entity2 = Story.builder()
                .id(2L)
                .editorId(20L)
                .title("Min Title")
                .content("Min Content.")
                .build();
        StoryOut output2 = StoryOut.builder()
                .editorId(20L)
                .title("Min Title")
                .content("Min Content.")
                .build();

        return Stream.of(
                Arguments.of(input1, entity1, output1),
                Arguments.of(input2, entity2, output2)
        );
    }

    @ParameterizedTest(name = "Create story with title: {2}")
    @MethodSource("creationTestData")
    void createShouldReturnStoryOutOnSuccessParameterized(StoryIn input, Story entity, StoryOut expectedOut) {
        // Act
        StoryOut actual = storyService.create(input);

        // Assert (Проверяем только поля, переданные в In, без ID/timestamps, как в прототипе)
        assertNotNull(actual);
        assertEquals(expectedOut.getEditorId(), actual.getEditorId());
        assertEquals(expectedOut.getTitle(), actual.getTitle());
        assertEquals(expectedOut.getContent(), actual.getContent());
    }

    @Test
    void getShouldReturnStoryOutForValidId() {
        // Arrange (Создаем сущность для получения, как в прототипе)
        StoryOut storyOut = storyService.create(storyIn);

        // Act
        StoryOut actual = storyService.get(storyOut.getId());

        // Assert
        assertNotNull(actual);
    }

    @Test
    void updateShouldReturnStoryOutOnSuccess() {
        // Arrange (Сначала создаем сущность, чтобы получить ID, который нужен для Update)
        StoryOut createdStory = storyService.create(storyIn);

        // Изменяем данные в storyIn (обновляем заголовок и контент)
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";

        StoryIn updateInput = StoryIn.builder()
                .id(createdStory.getId()) // Добавляем ID созданной сущности
                .editorId(storyIn.getEditorId())
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        // Act
        StoryOut actual = storyService.update(updateInput);

        // Assert (Проверяем соответствие DTO после обновления)
        assertNotNull(actual);
        assertEquals(updateInput.getTitle(), actual.getTitle());
        assertEquals(updateInput.getContent(), actual.getContent());
    }

    @Test
    void getAllShouldReturnListOfStoryOut() {
        // Arrange (Гарантируем, что в БД есть данные)
        storyService.create(storyIn);

        // Act
        List<StoryOut> actual = storyService.getAll();

        // Assert
        assertNotNull(actual);
        // Проверяем, что список не пуст (или имеет разумный размер)
        assertTrue(actual.size() > 0);
    }

    @Test
    void getShouldThrowExceptionForInvalidId() {
        // Act & Assert
        assertThrows(java.util.NoSuchElementException.class, () -> storyService.get(1234567890L));
    }

    @Test
    void deleteShouldReturnTrueOnSuccess() {
        // Arrange (Создаем сущность для удаления)
        StoryOut storyOut = storyService.create(storyIn);

        // Act
        boolean actual = storyService.delete(storyOut.getId());

        // Assert
        assertTrue(actual);
    }
}