package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.tag.Tag;
import com.javarush.led.lesson10.model.tag.TagIn;
import com.javarush.led.lesson10.model.tag.TagOut;
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
public class TagServiceIT {

    private final TagService tagService;
    private TagIn tagIn;

    @BeforeEach
    void setUp() {
        // Подготовка данных для тестов get, update, delete
        tagIn = TagIn.builder()
                .name("test_tag_for_crud")
                .build();
    }

    // --- Поставщик данных для параметрического теста CREATE ---

    private static Stream<Arguments> creationTestData() {
        // Кейс 1: Стандартное создание тега
        TagIn input1 = TagIn.builder()
                .name("programming")
                .build();
        // Tag entity (для соответствия прототипу Arguments.of(In, Entity, Out))
        Tag entity1 = Tag.builder()
                .id(1L)
                .name("programming")
                .build();
        TagOut output1 = TagOut.builder()
                .name("programming")
                .build();

        // Кейс 2: Короткое имя тега
        TagIn input2 = TagIn.builder()
                .name("api")
                .build();
        Tag entity2 = Tag.builder()
                .id(2L)
                .name("api")
                .build();
        TagOut output2 = TagOut.builder()
                .name("api")
                .build();

        return Stream.of(
                Arguments.of(input1, entity1, output1),
                Arguments.of(input2, entity2, output2)
        );
    }

    @ParameterizedTest(name = "Create tag with name: {0}")
    @MethodSource("creationTestData")
    void createShouldReturnTagOutOnSuccessParameterized(TagIn input, Tag entity, TagOut expectedOut) {
        // Act
        TagOut actual = tagService.create(input);

        // Assert
        assertNotNull(actual);
        // Проверяем только поле name, как в EditorServiceIT
        assertEquals(expectedOut.getName(), actual.getName());
    }

    @Test
    void getShouldReturnTagOutForValidId() {
        // Arrange: Создаем сущность для получения
        TagOut tagOut = tagService.create(tagIn);

        // Act
        TagOut actual = tagService.get(tagOut.getId());

        // Assert
        assertNotNull(actual);
    }

    @Test
    void updateShouldReturnTagOutOnSuccess() {
        // Arrange: 1. Создаем сущность, чтобы получить ID.
        TagOut createdTag = tagService.create(tagIn);

        // Arrange: 2. Создаем DTO для обновления, включая ID и новые данные.
        String newName = "updated_test_tag";
        TagIn updateInput = TagIn.builder()
                .id(createdTag.getId())
                .name(newName)
                .build();

        // Act
        TagOut actual = tagService.update(updateInput);

        // Assert: Проверяем, что обновленное DTO соответствует входным данным
        assertNotNull(actual);
        assertEquals(updateInput.getName(), actual.getName());
    }

    @Test
    void getAllShouldReturnListOfTagOut() {
        // Arrange: Гарантируем наличие данных
        tagService.create(tagIn);

        // Act
        List<TagOut> actual = tagService.getAll();

        // Assert
        assertNotNull(actual);
        assertTrue(actual.size() > 0, "Список тегов не должен быть пустым");
    }

    @Test
    void getShouldThrowExceptionForInvalidId() {
        // Act & Assert
        // Используем заведомо несуществующий ID
        assertThrows(java.util.NoSuchElementException.class, () -> tagService.get(1234567890L));
    }

    @Test
    void deleteShouldReturnTrueOnSuccess() {
        // Arrange: Создаем тег для удаления
        TagOut tagOut = tagService.create(tagIn);

        // Act
        boolean actual = tagService.delete(tagOut.getId());

        // Assert
        assertTrue(actual);
    }
}