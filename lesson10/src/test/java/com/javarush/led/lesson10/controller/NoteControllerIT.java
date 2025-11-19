package com.javarush.led.lesson10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.note.NoteIn;
import com.javarush.led.lesson10.model.note.NoteOut;
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
class NoteControllerIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void flow_createAndReadNote() throws Exception {
        // 1. Create note via REST
        var newNote = NoteIn.builder()
                .storyId(101L)
                .content("This is a test note content.")
                .build();

        String responseJson = mockMvc.perform(post("/api/v1.0/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNote)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is("This is a test note content.")))
                .andReturn().getResponse().getContentAsString();

        // Extract ID
        var createdNote = objectMapper.readValue(responseJson, NoteOut.class);
        Long createdId = createdNote.getId();

        // 2. Read created note by ID
        mockMvc.perform(get("/api/v1.0/notes/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdId.intValue())))
                .andExpect(jsonPath("$.storyId", is(101)))
                .andExpect(jsonPath("$.content", is("This is a test note content.")));
    }

    @Test
    void flow_createUpdateAndRead() throws Exception {
        // 1. Setup: Create
        var initialNote = NoteIn.builder()
                .storyId(202L)
                .content("Initial content")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialNote)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var createdId = objectMapper.readValue(jsonResponse, NoteOut.class).getId();

        // 2. Action: Update
        var updateRequest = NoteIn.builder()
                .id(createdId)
                .storyId(202L)
                .content("Updated content version 2")
                .build();

        mockMvc.perform(put("/api/v1.0/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Updated content version 2")));

        // 3. Verify: Check via GET
        mockMvc.perform(get("/api/v1.0/notes/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Updated content version 2")))
                .andExpect(jsonPath("$.storyId", is(202)));
    }

    @Test
    void flow_createAndDelete() throws Exception {
        // 1. Setup: Create
        var noteToDel = NoteIn.builder()
                .storyId(303L)
                .content("Content to be deleted")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteToDel)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var id = objectMapper.readValue(jsonResponse, NoteOut.class).getId();

        // 2. Action: Delete
        mockMvc.perform(delete("/api/v1.0/notes/{id}", id))
                .andExpect(status().isNoContent());

        // 3. Verify: Expect 404
        mockMvc.perform(get("/api/v1.0/notes/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void flow_getAll() throws Exception {
        // 1. Create a few notes
        createNote(1L, "Note A");
        createNote(2L, "Note B");

        // 2. Request list
        mockMvc.perform(get("/api/v1.0/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].content").exists())
                .andExpect(jsonPath("$[1].content").exists());
    }

    // Helper
    private void createNote(Long storyId, String content) throws Exception {
        var dto = NoteIn.builder()
                .storyId(storyId)
                .content(content)
                .build();

        mockMvc.perform(post("/api/v1.0/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}