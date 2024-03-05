package vn.edu.tdtu.musicapplication.models.artist_request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EDocumentType;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LegalDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private EDocumentType type;
    private List<String> images;
    @ManyToOne
    @JoinColumn(name = "artistRequestId")
    private ArtistRequest artistRequest;
}
