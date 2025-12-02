package by.it.course.dc.impl.note;

import by.it.course.dc.impl.note.model.NoteEvent;
import by.it.course.dc.impl.note.model.NoteRequestTo;
import by.it.course.dc.impl.note.model.NoteResponseTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaServer {

    public static final String REQUEST_TOPIC = "InTopic";
    public static final String RESPONSE_TOPIC = "OutTopic";
    private final KafkaTemplate<String, String> sender;
    private final ObjectMapper json;
    private final NoteService noteService;


    @SneakyThrows
    @KafkaListener(topics = REQUEST_TOPIC, groupId = REQUEST_TOPIC)
    private void processNote(ConsumerRecord<String, String> record) {
        UUID uuid = UUID.fromString(record.key());
        NoteEvent noteEvent = json.readValue(record.value(), NoteEvent.class);
        NoteRequestTo noteRequestTo = noteEvent.noteRequestTo();
        List<NoteResponseTo> noteResponseTos = switch (noteEvent.operation()) {
            case FIND_ALL -> noteService.findAll();
            case FIND_BY_ID -> List.of(noteService.findById(noteEvent.idData()));
            case CREATE -> List.of(noteService.create(noteRequestTo, Locale.getDefault()));
            case UPDATE -> List.of(noteService.update(noteRequestTo));
            case REMOVE_BY_ID -> {
                noteService.removeById(noteEvent.idData());
                yield List.of();
            }
        };
        NoteEvent result = new NoteEvent(noteResponseTos);
        sender.send(RESPONSE_TOPIC, uuid.toString(), json.writeValueAsString(result));
    }
}
