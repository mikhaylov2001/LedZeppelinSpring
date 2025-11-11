package com.javarush.led.lesson08.controller;

import com.javarush.led.lesson08.model.editor.EditorIn;
import com.javarush.led.lesson08.model.editor.EditorOut;
import com.javarush.led.lesson08.service.EditorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1.0/editors")
public class EditorController {

    private final EditorService editorService;

    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }

    @GetMapping
    public Collection<EditorOut> getAll() {
        return editorService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditorOut create(@RequestBody @Valid EditorIn inputDto) {
        return editorService.create(inputDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EditorOut update(@RequestBody @Valid EditorIn inputDto) {
        try {
            return editorService.update(inputDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public EditorOut read(@PathVariable long id) {
        return editorService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        boolean delete = editorService.delete(id);
        if (!delete) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
