package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.mapper.EditorDto;
import com.javarush.led.lesson10.model.editor.Editor;
import com.javarush.led.lesson10.model.editor.EditorIn;
import com.javarush.led.lesson10.model.editor.EditorOut;
import com.javarush.led.lesson10.repository.EditorRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditorServiceTest {

    @Mock
    private EditorRepo repoImpl;

    @Mock
    private EditorDto mapper;

    @InjectMocks
    private EditorService editorService;

    private final Long TEST_ID = 1L;
    private Editor mockEntity;
    private EditorOut mockOut;
    private EditorIn mockIn;

    @BeforeEach
    void setUp() {
        mockEntity = Editor.builder()
                .id(TEST_ID)
                .login("test_user")
                .password("secure_pass123")
                .firstname("Test")
                .lastname("User")
                .build();

        mockOut = EditorOut.builder()
                .id(TEST_ID)
                .login("test_user")
                .firstname("Test")
                .lastname("User")
                .build();

        mockIn = EditorIn.builder()
                .id(TEST_ID)
                .login("new_user")
                .password("new_secure_pass123")
                .firstname("New")
                .lastname("User")
                .build();
    }

    // --- Параметрический тест для CREATE ---

    private static Stream<Arguments> creationTestData() {
        // Scenario 1: New Editor
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

        // Scenario 2: Another New Editor
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
        when(mapper.in(input)).thenReturn(entity);
        when(repoImpl.create(entity)).thenReturn(Optional.of(entity));
        when(mapper.out(entity)).thenReturn(expectedOut);

        EditorOut actual = editorService.create(input);

        assertNotNull(actual);
        assertEquals(expectedOut, actual);

        verify(mapper, times(1)).in(input);
        verify(repoImpl, times(1)).create(entity);
        verify(mapper, times(1)).out(entity);
    }

    // --- Тест для UPDATE (используем mockIn/mockEntity/mockOut из setup) ---

    @Test
    void updateShouldReturnEditorOutOnSuccess() {
        when(mapper.in(mockIn)).thenReturn(mockEntity);
        when(repoImpl.update(mockEntity.getId(), mockEntity)).thenReturn(Optional.of(mockEntity));
        when(mapper.out(mockEntity)).thenReturn(mockOut);

        EditorOut actual = editorService.update(mockIn);

        assertNotNull(actual);
        assertEquals(mockOut, actual);

        verify(mapper, times(1)).in(mockIn);
        verify(repoImpl, times(1)).update(mockEntity.getId(), mockEntity);
        verify(mapper, times(1)).out(mockEntity);
    }

    // --- Другие тесты ---

    @Test
    void getAllShouldReturnListOfEditorOut() {
        List<Editor> entities = List.of(mockEntity, mockEntity);
        List<EditorOut> expected = List.of(mockOut, mockOut);

        when(repoImpl.getAll()).thenReturn(entities.stream());
        when(mapper.out(mockEntity)).thenReturn(mockOut);

        List<EditorOut> actual = editorService.getAll();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());

        verify(repoImpl, times(1)).getAll();
        verify(mapper, times(entities.size())).out(mockEntity);
    }

    @Test
    void getShouldReturnEditorOutForValidId() {
        when(repoImpl.get(TEST_ID)).thenReturn(Optional.of(mockEntity));
        when(mapper.out(mockEntity)).thenReturn(mockOut);

        EditorOut actual = editorService.get(TEST_ID);

        assertNotNull(actual);
        assertEquals(mockOut, actual);

        verify(repoImpl, times(1)).get(TEST_ID);
        verify(mapper, times(1)).out(mockEntity);
    }

    @Test
    void getShouldThrowExceptionForInvalidId() {
        when(repoImpl.get(TEST_ID)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> editorService.get(TEST_ID));

        verify(repoImpl, times(1)).get(TEST_ID);
        verify(mapper, times(0)).out(any());
    }

    @Test
    void deleteShouldReturnTrueOnSuccess() {
        when(repoImpl.delete(TEST_ID)).thenReturn(true);

        boolean actual = editorService.delete(TEST_ID);

        assertTrue(actual);

        verify(repoImpl, times(1)).delete(TEST_ID);
    }
}