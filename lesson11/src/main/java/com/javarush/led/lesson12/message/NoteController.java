package com.javarush.led.lesson12.message;


import com.javarush.led.lesson12.message.model.NoteRequestTo;
import com.javarush.led.lesson12.message.model.NoteResponseTo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1.0/notes")
@Data
@AllArgsConstructor
@Valid
public class NoteController {

    private NoteService noteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NoteResponseTo> findAll() {
        return noteService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponseTo findOne(@PathVariable("id") long id) {
        return noteService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponseTo create(@Valid @RequestBody NoteRequestTo dto, Locale locale) {
        return noteService.create(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NoteResponseTo update(@Valid @RequestBody NoteRequestTo dto) {
        return noteService.update(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        noteService.removeById(id);
    }
}