package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.mapper.NoteDto;
import com.javarush.led.lesson10.model.note.NoteIn;
import com.javarush.led.lesson10.model.note.NoteOut;
import com.javarush.led.lesson10.repository.NoteRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteService {

    public final NoteRepo noteRepo;
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
        return noteRepo
                .create(mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public NoteOut update(NoteIn input) {
        return noteRepo
                .update(input.getId(), mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public boolean delete(Long id) {
        return noteRepo.delete(id);
    }
}
