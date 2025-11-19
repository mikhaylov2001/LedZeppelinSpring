package com.javarush.led.lesson10.controller;

import com.javarush.led.lesson10.model.tag.TagIn;
import com.javarush.led.lesson10.model.tag.TagOut;
import com.javarush.led.lesson10.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1.0/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public Collection<TagOut> getAll() {
        return tagService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagOut create(@RequestBody @Valid TagIn inputDto) {
        return tagService.create(inputDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TagOut update(@RequestBody @Valid TagIn inputDto) {
        try {
            return tagService.update(inputDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public TagOut read(@PathVariable long id) {
        return tagService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        boolean delete = tagService.delete(id);
        if (!delete) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
