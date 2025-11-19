package com.javarush.led.lesson10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.editor.EditorIn;
import com.javarush.led.lesson10.model.editor.EditorOut;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor
class EditorControllerIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void flow_createAndReadEditor() throws Exception {
        // 1. Create user via REST
        var newEditor = EditorIn.builder()
                .login("system_user")
                .password("strongPass1")
                .firstname("System")
                .lastname("Test")
                .build();

        String responseJson = mockMvc.perform(post("/api/v1.0/editors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEditor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login", is("system_user")))
                .andReturn().getResponse().getContentAsString();

        // Extract ID from response
        var createdEditor = objectMapper.readValue(responseJson, EditorOut.class);
        Long createdId = createdEditor.getId();

        // 2. Read created user via REST by ID
        mockMvc.perform(get("/api/v1.0/editors/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdId.intValue())))
                .andExpect(jsonPath("$.firstname", is("System")));
    }

    @Test
    void flow_createUpdateAndRead() throws Exception {
        // 1. Setup: Create via REST
        var initialEditor = EditorIn.builder()
                .login("before_update")
                .password("pass1234")
                .firstname("OldName")
                .lastname("OldLast")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/editors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialEditor)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var createdId = objectMapper.readValue(jsonResponse, EditorOut.class).getId();

        // 2. Action: Update via REST
        var updateRequest = EditorIn.builder()
                .id(createdId)
                .login("after_update")
                .password("newPass1234")
                .firstname("NewName")
                .lastname("OldLast")
                .build();

        mockMvc.perform(put("/api/v1.0/editors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("after_update")));

        // 3. Verify: Check via GET that data actually changed
        mockMvc.perform(get("/api/v1.0/editors/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", is("NewName")))
                .andExpect(jsonPath("$.login", is("after_update")));
    }

    @Test
    void flow_createAndDelete() throws Exception {
        // 1. Setup: Create
        var editorToDel = EditorIn.builder()
                .login("temp_user")
                .password("12345678")
                .firstname("Temp")
                .lastname("User")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/editors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editorToDel)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var id = objectMapper.readValue(jsonResponse, EditorOut.class).getId();

        // 2. Action: Delete
        mockMvc.perform(delete("/api/v1.0/editors/{id}", id))
                .andExpect(status().isNoContent());

        // 3. Verify: Attempt to retrieve and expect 404
        mockMvc.perform(get("/api/v1.0/editors/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void flow_getAll() throws Exception {
        // 1. Create a few users
        createEditor("user1", "John");
        createEditor("user2", "Jane");

        // 2. Request list
        mockMvc.perform(get("/api/v1.0/editors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login").exists())
                .andExpect(jsonPath("$[1].login").exists());
    }

    // Helper for boilerplate reduction
    private void createEditor(String login, String firstname) throws Exception {
        var dto = EditorIn.builder()
                .login(login)
                .password("defaultPass")
                .firstname(firstname)
                .lastname("Default")
                .build();

        mockMvc.perform(post("/api/v1.0/editors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}