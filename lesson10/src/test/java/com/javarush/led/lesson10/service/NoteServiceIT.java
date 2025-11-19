package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.note.Note;
import com.javarush.led.lesson10.model.note.NoteIn;
import com.javarush.led.lesson10.model.note.NoteOut;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// Вспомогательный класс NoteOut для компиляции, поскольку его нет в файлах
// В реальном проекте должен быть импортирован.
// (Предполагаем, что NoteOut имеет те же поля, что и Note, кроме ID при создании)
// @Data @Builder @NoArgsConstructor @AllArgsConstructor
// class NoteOut { Long id; Long storyId; String content; } 

@IntegrationTest
@RequiredArgsConstructor
public class NoteServiceIT {

    private final NoteService noteService;
    private NoteIn initialNoteIn;
    private final Long TEST_STORY_ID = 99L;

    @BeforeEach
    void setUp() {
        // Подготовка данных для тестов get, update, delete
        initialNoteIn = NoteIn.builder()
                .storyId(TEST_STORY_ID)
                .content("Initial note content for CRUD tests.")
                .build();
    }

    // --- Поставщик данных для параметрического теста CREATE ---

    private static Stream<Arguments> creationTestData() {
        // Кейс 1: Стандартная заметка
        NoteIn input1 = NoteIn.builder()
                .storyId(101L)
                .content("First blog note content with minimum length.")
                .build();
        // В интеграционных тестах (IT) мы не мокаем сущности,
        // но включаем их в Arguments для соответствия прототипу EditorServiceIT
        Note entity1 = Note.builder()
                .storyId(101L)
                .content("First blog note content with minimum length.")
                .build();
        NoteOut output1 = NoteOut.builder() // Ожидаемый вывод
                .storyId(101L)
                .content("First blog note content with minimum length.")
                .build();

        // Кейс 2: Короткий контент
        NoteIn input2 = NoteIn.builder()
                .storyId(202L)
                .content("Short comment.")
                .build();
        Note entity2 = Note.builder()
                .storyId(202L)
                .content("Short comment.")
                .build();
        NoteOut output2 = NoteOut.builder()
                .storyId(202L)
                .content("Short comment.")
                .build();

        return Stream.of(
                Arguments.of(input1, entity1, output1),
                Arguments.of(input2, entity2, output2)
        );
    }

    @ParameterizedTest(name = "Create note for story ID: {1}")
    @MethodSource("creationTestData")
    void createShouldReturnNoteOutOnSuccessParameterized(NoteIn input, Note entity, NoteOut expectedOut) {
        // Act
        NoteOut actual = noteService.create(input);

        // Assert
        assertNotNull(actual);
        assertNotNull(actual.getId(), "ID должен быть сгенерирован БД");
        assertEquals(expectedOut.getStoryId(), actual.getStoryId());
        assertEquals(expectedOut.getContent(), actual.getContent());
    }

    @Test
    void getShouldReturnNoteOutForValidId() {
        // Arrange: Сначала создаем сущность, чтобы она была в БД
        NoteOut createdNote = noteService.create(initialNoteIn);

        // Act
        NoteOut actual = noteService.get(createdNote.getId());

        // Assert
        assertNotNull(actual);
        assertEquals(createdNote.getId(), actual.getId());
        assertEquals(initialNoteIn.getContent(), actual.getContent());
    }

    @Test
    void updateShouldReturnNoteOutOnSuccess() {
        // Arrange: Создаем и получаем ID
        NoteOut createdNote = noteService.create(initialNoteIn);

        String newContent = "Content after successful update.";

        // Arrange: Готовим входной объект для обновления, включая ID
        NoteIn updateNoteIn = NoteIn.builder()
                .id(createdNote.getId())
                .storyId(createdNote.getStoryId())
                .content(newContent)
                .build();

        // Act
        NoteOut actual = noteService.update(updateNoteIn);

        // Assert
        assertNotNull(actual);
        assertEquals(createdNote.getId(), actual.getId());
        assertEquals(newContent, actual.getContent());

        // Проверяем, что изменения зафиксированы в БД
        NoteOut fetchedNote = noteService.get(actual.getId());
        assertEquals(newContent, fetchedNote.getContent());
    }

    @Test
    void getAllShouldReturnListOfNoteOut() {
        // Arrange: Обеспечиваем наличие данных для получения
        noteService.create(initialNoteIn);
        noteService.create(NoteIn.builder().storyId(1L).content("second note").build());

        // Act
        List<NoteOut> actual = noteService.getAll();

        // Assert
        assertNotNull(actual);
        assertTrue(actual.size() >= 2, "Должно быть найдено минимум 2 заметки");
    }

    @Test
    void getShouldThrowExceptionForInvalidId() {
        // Act & Assert
        // Используем заведомо несуществующий ID
        assertThrows(java.util.NoSuchElementException.class, () -> noteService.get(1234567890L));
    }

    @Test
    void deleteShouldReturnTrueOnSuccess() {
        // Arrange: Создаем заметку для удаления
        NoteOut createdNote = noteService.create(initialNoteIn);

        // Act
        boolean actual = noteService.delete(createdNote.getId());

        // Assert
        assertTrue(actual, "Удаление должно вернуть true");

        // Проверяем, что заметка действительно удалена
        assertThrows(java.util.NoSuchElementException.class, () -> noteService.get(createdNote.getId()));
    }
}