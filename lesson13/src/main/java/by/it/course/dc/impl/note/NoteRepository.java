package by.it.course.dc.impl.note;

import by.it.course.dc.impl.note.model.Note;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CassandraRepository<Note, Long> {

    @Override
    @Query("select * from tbl_note")
    List<Note> findAll();

    @AllowFiltering
    Optional<Note> findById(Long aLong);

}

