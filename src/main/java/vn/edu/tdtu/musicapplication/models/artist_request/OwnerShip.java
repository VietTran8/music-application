package vn.edu.tdtu.musicapplication.models.artist_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "artistRequestId")
    @JsonIgnore
    private ArtistRequest artistRequest;
}