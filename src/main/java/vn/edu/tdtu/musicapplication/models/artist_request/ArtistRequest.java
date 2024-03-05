package vn.edu.tdtu.musicapplication.models.artist_request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Boolean isAccepted;
    private LocalDateTime requestedDate;
    private List<String> works;
    private List<String> ownerships;
    @OneToMany(mappedBy = "artistRequest")
    private List<LegalDocument> legalDocuments;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @OneToOne
    @JoinColumn(name = "personalInfoId")
    private PersonalInfo personalInfo;
}
