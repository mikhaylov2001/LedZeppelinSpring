package by.it.course.dc.impl.note.model;

public record NoteResponseTo(
        Long id,
        Long storyId,
        String content
) {
}
