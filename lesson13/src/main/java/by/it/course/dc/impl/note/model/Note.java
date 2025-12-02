package by.it.course.dc.impl.note.model;

import by.it.course.dc.impl.note.IdHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("tbl_note")
public class Note implements IdHolder {

    @PrimaryKeyColumn(
            name = "country",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    private String country;

    @PrimaryKeyColumn(
            name = "id",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED
    )
    private Long id;

    @PrimaryKeyColumn(
            name = "story_id",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED
    )
    private Long storyId;

    private String content;


}