package by.it.course.dc.impl.note.model;

import java.util.List;

public record NoteEvent(
        Operation operation,
        Long idData,
        NoteRequestTo noteRequestTo,
        List<NoteResponseTo> noteResponseTos
        ) {
    public NoteEvent(Operation operation) {
        this(operation, null, null, null);
    }

    public NoteEvent(Operation operation, Long idData) {
        this(operation, idData, null, null);
    }

    public NoteEvent(Operation operation, NoteRequestTo noteRequestTo) {
        this(operation, null, noteRequestTo, null);
    }

    public NoteEvent(List<NoteResponseTo> noteResponseTos) {
        this(null, null, null, noteResponseTos);
    }

    public enum Operation {
        FIND_ALL,
        FIND_BY_ID,
        CREATE,
        UPDATE,
        REMOVE_BY_ID,
    }

}
