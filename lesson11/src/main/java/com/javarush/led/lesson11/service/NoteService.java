package com.javarush.led.lesson11.service;

import com.javarush.led.lesson11.mapper.NoteDto;
import com.javarush.led.lesson11.model.note.Note;
import com.javarush.led.lesson11.model.note.NoteIn;
import com.javarush.led.lesson11.model.note.NoteOut;
import com.javarush.led.lesson11.model.story.Story;
import com.javarush.led.lesson11.repository.NoteRepo;
import com.javarush.led.lesson11.repository.StoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
public class NoteService {

    public final NoteRepo noteRepo;
    public final StoryRepo storyRepo;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public final NoteDto mapper;

    public List<NoteOut> getAll() {
        return noteRepo
                .getAll()
                .map(mapper::out)
                .toList();
    }

    public NoteOut get(Long id) {
        return noteRepo
                .get(id)
                .map(mapper::out)
                .orElseThrow();
    }

    public NoteOut create(NoteIn input) {
        Note note = prepareNote(input);
        return noteRepo
                .create(note)
                .map(mapper::out)
                .orElseThrow();
    }

    public NoteOut update(NoteIn input) {
        Note note = prepareNote(input);
        return noteRepo
                .update(note)
                .map(mapper::out)
                .orElseThrow();
    }

    private Note prepareNote(NoteIn input) {
        Story story = storyRepo.get(input.getStoryId()).orElseThrow();
        Note note = mapper.in(input);
        note.setStory(story);
        return note;
    }

    public boolean delete(Long id) {
        return noteRepo.delete(id);
    }
}
