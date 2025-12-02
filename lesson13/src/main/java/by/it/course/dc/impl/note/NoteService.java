package by.it.course.dc.impl.note;

import by.it.course.dc.impl.note.model.Note;
import by.it.course.dc.impl.note.model.NoteRequestTo;
import by.it.course.dc.impl.note.model.NoteResponseTo;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteMapper noteMapper;

    private final NoteRepository noteRepository;

    public List<NoteResponseTo> findAll() {

        val notes = noteRepository.findAll();
        return noteMapper.getListResponseTo(notes);
    }

    public NoteResponseTo findById(long id) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        return noteMapper.getResponseTo(optionalNote.orElseThrow());
    }

    public NoteResponseTo create(NoteRequestTo noteRequestTo, Locale locale) {
        Note note = noteMapper.getEntity(noteRequestTo);
        long id = generateId();
        note.setId(id);
        note.setCountry(locale.getCountry());
        note = noteRepository.save(note);
        return Optional.of(note)
                .map(noteMapper::getResponseTo)
                .orElseThrow();
    }

    private static long generateId() {
        long id = System.currentTimeMillis();
        int randomBitsCount = 64 - 44;
        int rnd = ThreadLocalRandom.current().nextInt(1 << randomBitsCount);
        id = (id << randomBitsCount) + rnd;
        return id;
    }

    public NoteResponseTo update(NoteRequestTo noteRequestTo) {
        Note note = noteMapper.getEntity(noteRequestTo);
        Optional<Note> optionalNote = noteRepository.findById(note.getId());
        if (optionalNote.isPresent()) {
            Note inDbNote = optionalNote.get();
            if (note.getCountry() == null) {
                note.setCountry(inDbNote.getCountry());
            }
            noteRepository.save(note);
            return Optional.of(note)
                    .map(noteMapper::getResponseTo)
                    .orElseThrow();
        } else throw new NoSuchElementException("by id=" + note.getId());
    }

    public void removeById(Long id) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            noteRepository.delete(note);
        } else throw new NoSuchElementException("by id=" + id);
    }

}