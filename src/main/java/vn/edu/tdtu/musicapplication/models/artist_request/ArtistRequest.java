package vn.edu.tdtu.musicapplication.models.artist_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EArtistRequestStatus;
import vn.edu.tdtu.musicapplication.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ArtistRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private EArtistRequestStatus status;
    private Boolean active;
    private LocalDateTime requestedDate;
    @OneToMany(mappedBy = "artistRequest", cascade = CascadeType.ALL)
    private List<Work> works;
    @OneToMany(mappedBy = "artistRequest", cascade = CascadeType.ALL)
    private List<OwnerShip> ownerships;
    @OneToMany(mappedBy = "artistRequest", cascade = CascadeType.ALL)
    private List<LegalDocument> legalDocuments;
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artistInfoId")
    private ArtistInfo artistInfo;
}