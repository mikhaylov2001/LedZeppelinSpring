package com.javarush.led.lesson08.repository;

import com.javarush.led.lesson08.model.note.Note;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Repository
public class NoteRepoImpl implements Repo<Note> {

    Map<Long, Note> memoryDatabase = new ConcurrentHashMap<>();
    public static final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Stream<Note> getAll() {
        return memoryDatabase.values().stream();
    }

    @Override
    public Optional<Note> get(Long id) {
        return Optional.ofNullable(memoryDatabase.get(id));
    }

    @Override
    public Optional<Note> create(Note input) {
        long id = idGenerator.incrementAndGet();
        input.setId(id);
        memoryDatabase.put(id, input);
        return Optional.of(input);
    }

    @Override
    public Optional<Note> update(Note input) {
        memoryDatabase.put(input.getId(), input);
        return Optional.of(input);
    }

    @Override
    public boolean delete(Long id) {
        return memoryDatabase.remove(id) != null;
    }
}
