package com.javarush.led.lesson10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.story.StoryIn;
import com.javarush.led.lesson10.model.story.StoryOut;
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
class StoryControllerIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void flow_createAndReadStory() throws Exception {
        // 1. Create story via REST
        var newStory = StoryIn.builder()
                .editorId(100L)
                .title("Test Story Title")
                .content("This is the content of the test story.")
                .build();

        String responseJson = mockMvc.perform(post("/api/v1.0/stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Story Title")))
                .andReturn().getResponse().getContentAsString();

        // Extract ID
        var createdStory = objectMapper.readValue(responseJson, StoryOut.class);
        Long createdId = createdStory.getId();

        // 2. Read created story via REST by ID
        mockMvc.perform(get("/api/v1.0/stories/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdId.intValue())))
                .andExpect(jsonPath("$.editorId", is(100)))
                .andExpect(jsonPath("$.title", is("Test Story Title")));
    }

    @Test
    void flow_createUpdateAndRead() throws Exception {
        // 1. Setup: Create
        var initialStory = StoryIn.builder()
                .editorId(200L)
                .title("Initial Title")
                .content("Initial content with enough length.")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialStory)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var createdId = objectMapper.readValue(jsonResponse, StoryOut.class).getId();

        // 2. Action: Update
        var updateRequest = StoryIn.builder()
                .id(createdId)
                .editorId(200L)
                .title("Updated Title")
                .content("Updated content version 2.")
                .build();

        mockMvc.perform(put("/api/v1.0/stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")));

        // 3. Verify: Check via GET
        mockMvc.perform(get("/api/v1.0/stories/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.content", is("Updated content version 2.")));
    }

    @Test
    void flow_createAndDelete() throws Exception {
        // 1. Setup: Create
        var storyToDel = StoryIn.builder()
                .editorId(300L)
                .title("To Be Deleted")
                .content("Content that will vanish soon.")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storyToDel)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var id = objectMapper.readValue(jsonResponse, StoryOut.class).getId();

        // 2. Action: Delete
        mockMvc.perform(delete("/api/v1.0/stories/{id}", id))
                .andExpect(status().isNoContent());

        // 3. Verify: Expect 404
        mockMvc.perform(get("/api/v1.0/stories/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void flow_getAll() throws Exception {
        // 1. Create a few stories
        createStory(10L, "Story One", "Content for story one.");
        createStory(20L, "Story Two", "Content for story two.");

        // 2. Request list
        mockMvc.perform(get("/api/v1.0/stories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[1].title").exists());
    }

    // Helper
    private void createStory(Long editorId, String title, String content) throws Exception {
        var dto = StoryIn.builder()
                .editorId(editorId)
                .title(title)
                .content(content)
                .build();

        mockMvc.perform(post("/api/v1.0/stories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}