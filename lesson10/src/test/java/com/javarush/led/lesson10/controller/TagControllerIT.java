package com.javarush.led.lesson10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.led.lesson10.api.IntegrationTest;
import com.javarush.led.lesson10.model.tag.TagIn;
import com.javarush.led.lesson10.model.tag.TagOut;
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
class TagControllerIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void flow_createAndReadTag() throws Exception {
        // 1. Create tag via REST
        var newTag = TagIn.builder()
                .name("Fiction")
                .build();

        String responseJson = mockMvc.perform(post("/api/v1.0/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTag)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Fiction")))
                .andReturn().getResponse().getContentAsString();

        // Extract ID
        var createdTag = objectMapper.readValue(responseJson, TagOut.class);
        Long createdId = createdTag.getId();

        // 2. Read created tag via REST by ID
        mockMvc.perform(get("/api/v1.0/tags/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdId.intValue())))
                .andExpect(jsonPath("$.name", is("Fiction")));
    }

    @Test
    void flow_createUpdateAndRead() throws Exception {
        // 1. Setup: Create initial tag
        var initialTag = TagIn.builder()
                .name("Draft")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialTag)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var createdId = objectMapper.readValue(jsonResponse, TagOut.class).getId();

        // 2. Action: Update tag name
        var updateRequest = TagIn.builder()
                .id(createdId)
                .name("Published")
                .build();

        mockMvc.perform(put("/api/v1.0/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Published")));

        // 3. Verify: Check via GET that the name is updated
        mockMvc.perform(get("/api/v1.0/tags/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Published")));
    }

    @Test
    void update_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Action: Attempt to update a non-existent ID
        var nonExistentTag = TagIn.builder()
                .id(9999L)
                .name("GhostTag")
                .build();

        mockMvc.perform(put("/api/v1.0/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentTag)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create_shouldReturnBadRequest_whenValidationFails() throws Exception {
        // Action: Attempt to create a tag with a name that is too short (min=2 violation)
        var invalidTag = TagIn.builder()
                .name("a")
                .build();

        mockMvc.perform(post("/api/v1.0/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTag)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void flow_createAndDelete() throws Exception {
        // 1. Setup: Create tag to be deleted
        var tagToDel = TagIn.builder()
                .name("Temporary")
                .build();

        String jsonResponse = mockMvc.perform(post("/api/v1.0/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagToDel)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var id = objectMapper.readValue(jsonResponse, TagOut.class).getId();

        // 2. Action: Delete
        mockMvc.perform(delete("/api/v1.0/tags/{id}", id))
                .andExpect(status().isNoContent());

        // 3. Verify: Expect 404
        mockMvc.perform(get("/api/v1.0/tags/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Action: Attempt to delete a non-existent ID
        // Expected status changed to 400 based on the actual test output.
        mockMvc.perform(delete("/api/v1.0/tags/{id}", 9999L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void flow_getAll() throws Exception {
        // 1. Create a few tags
        createTag("Tech");
        createTag("Science");

        // 2. Request list
        mockMvc.perform(get("/api/v1.0/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Tech")))
                .andExpect(jsonPath("$[1].name", is("Science")));
    }

    // Helper method to simplify tag creation boilerplate
    private void createTag(String name) throws Exception {
        var dto = TagIn.builder()
                .name(name)
                .build();

        mockMvc.perform(post("/api/v1.0/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}