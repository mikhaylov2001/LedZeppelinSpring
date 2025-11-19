package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.editor.Editor;
import com.javarush.led.lesson10.model.editor.EditorIn;
import com.javarush.led.lesson10.model.editor.EditorOut;
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
public class EditorServiceIT{

    private final EditorService editorService;
    private EditorIn editorIn;

    @BeforeEach
    void setUp() {
        editorIn = EditorIn.builder()
                .login("new_user")
                .password("new_secure_pass123")
                .firstname("New")
                .lastname("User")
                .build();
    }

    private static Stream<Arguments> creationTestData() {
        EditorIn input1 = EditorIn.builder()
                .login("john.doe")
                .password("strongPassword!1")
                .firstname("John")
                .lastname("Doe")
                .build();
        Editor entity1 = Editor.builder()
                .id(10L)
                .login("john.doe")
                .password("strongPassword!1")
                .firstname("John")
                .lastname("Doe")
                .build();
        EditorOut output1 = EditorOut.builder()
                .id(10L)
                .login("john.doe")
                .firstname("John")
                .lastname("Doe")
                .build();

        EditorIn input2 = EditorIn.builder()
                .login("alice_w")
                .password("AlicePass321")
                .firstname("Alice")
                .lastname("Wonder")
                .build();
        Editor entity2 = Editor.builder()
                .id(11L)
                .login("alice_w")
                .password("AlicePass321")
                .firstname("Alice")
                .lastname("Wonder")
                .build();
        EditorOut output2 = EditorOut.builder()
                .id(11L)
                .login("alice_w")
                .firstname("Alice")
                .lastname("Wonder")
                .build();

        return Stream.of(
                Arguments.of(input1, entity1, output1),
                Arguments.of(input2, entity2, output2)
        );
    }

    @ParameterizedTest(name = "Create with login: {0}")
    @MethodSource("creationTestData")
    void createShouldReturnEditorOutOnSuccessParameterized(EditorIn input, Editor entity, EditorOut expectedOut) {
        EditorOut actual = editorService.create(input);
        assertNotNull(actual);
        assertEquals(expectedOut.getLogin(), actual.getLogin());
        assertEquals(expectedOut.getFirstname(), actual.getFirstname());
        assertEquals(expectedOut.getLastname(), actual.getLastname());

    }

    @Test
    void getShouldReturnEditorOutForValidId() {
        EditorOut editorOut = editorService.create(editorIn);
        EditorOut actual = editorService.get(editorOut.getId());
        assertNotNull(actual);
    }

    @Test
    void updateShouldReturnEditorOutOnSuccess() {
        EditorOut editorOut = editorService.create(editorIn);
        editorIn.setId(editorOut.getId());
        EditorOut actual = editorService.update(editorIn);
        assertNotNull(actual);
        assertEquals(editorIn.getLogin(), actual.getLogin());
        assertEquals(editorIn.getFirstname(), actual.getFirstname());
        assertEquals(editorIn.getLastname(), actual.getLastname());

    }

    @Test
    void getAllShouldReturnListOfEditorOut() {
        List<EditorOut> actual = editorService.getAll();
        assertNotNull(actual);

    }

    @Test
    void getShouldThrowExceptionForInvalidId() {
        assertThrows(java.util.NoSuchElementException.class, () -> editorService.get(1234567890L));
    }

    @Test
    void deleteShouldReturnTrueOnSuccess() {
        EditorOut editorOut = editorService.create(editorIn);
        boolean actual = editorService.delete(editorOut.getId());
        assertTrue(actual);
    }
}