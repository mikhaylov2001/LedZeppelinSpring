package com.javarush.led.lesson08.controller;

import com.javarush.led.lesson08.model.story.StoryIn;
import com.javarush.led.lesson08.model.story.StoryOut;
import com.javarush.led.lesson08.service.StoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1.0/stories")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping
    public Collection<StoryOut> getAll() {
        return storyService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoryOut create(@RequestBody @Valid StoryIn inputDto) {
        return storyService.create(inputDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public StoryOut update(@RequestBody @Valid StoryIn inputDto) {
        try {
            return storyService.update(inputDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public StoryOut read(@PathVariable long id) {
        return storyService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        boolean delete = storyService.delete(id);
        if (!delete) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
