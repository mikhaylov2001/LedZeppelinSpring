package com.javarush.led.lesson08.service;

import com.javarush.led.lesson08.mapper.NoteDto;
import com.javarush.led.lesson08.model.note.NoteIn;
import com.javarush.led.lesson08.model.note.NoteOut;
import com.javarush.led.lesson08.repository.NoteRepoImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteService {

    public final NoteRepoImpl repoImpl;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public final NoteDto mapper;

    public List<NoteOut> getAll() {
        return repoImpl
                .getAll()
                .map(mapper::out)
                .toList();
    }

    public NoteOut get(Long id) {
        return repoImpl
                .get(id)
                .map(mapper::out)
                .orElseThrow();
    }

    public NoteOut create(NoteIn input) {
        return repoImpl
                .create(mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public NoteOut update(NoteIn input) {
        return repoImpl
                .update(mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public boolean delete(Long id) {
        return repoImpl.delete(id);
    }
}
