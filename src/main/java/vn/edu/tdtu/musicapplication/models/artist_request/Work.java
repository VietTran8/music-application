package vn.edu.tdtu.musicapplication.models.artist_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.utils.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime releaseDate;
    private String author;
    @ManyToOne
    @JoinColumn(name = "artistRequestId")
    @JsonIgnore
    private ArtistRequest artistRequest;
}
