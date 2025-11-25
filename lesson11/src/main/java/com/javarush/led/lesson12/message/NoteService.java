package com.javarush.led.lesson12.message;


import com.javarush.led.lesson11.repository.StoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.javarush.led.lesson12.message.model.NoteRequestTo;
import com.javarush.led.lesson12.message.model.NoteResponseTo;


import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService implements RestService<NoteRequestTo, NoteResponseTo> {

    public static final ParameterizedTypeReference<List<NoteResponseTo>> LIST_NOTE_RESPONSE_TO =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient noteRestClient;

    private final StoryRepo storyRepository;


    @Override
    public List<NoteResponseTo> findAll() {
        return noteRestClient
                .get()
                .retrieve()
                .body(LIST_NOTE_RESPONSE_TO);
    }

    @Override
    public NoteResponseTo findById(long noteId) {
        return noteRestClient
                .get()
                .uri("/{noteId}", noteId)
                .retrieve()
                .body(NoteResponseTo.class);
    }

    @Override
    public NoteResponseTo create(NoteRequestTo noteRequestTo) {
        Long storyId = noteRequestTo.storyId();
        if (storyRepository.existsById(storyId)) {
            return noteRestClient
                    .post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(noteRequestTo)
                    .retrieve()
                    .body(NoteResponseTo.class);
        } else {
            throw new IllegalStateException("incorrect storyId=" + storyId);
        }
    }

    @Override
    public NoteResponseTo update(NoteRequestTo noteRequestTo) {
        Long storyId = noteRequestTo.storyId();
        if (storyRepository.existsById(storyId)) {
            return noteRestClient
                    .put()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(noteRequestTo)
                    .retrieve()
                    .body(NoteResponseTo.class);
        } else {
            throw new IllegalStateException("incorrect storyId=" + storyId);
        }
    }

    @Override
    public boolean removeById(Long noteId) {
        return noteRestClient
                .delete()
                .uri("/{noteId}", noteId)
                .retrieve()
                .toBodilessEntity()
                .getStatusCode()
                .is2xxSuccessful();
    }

}