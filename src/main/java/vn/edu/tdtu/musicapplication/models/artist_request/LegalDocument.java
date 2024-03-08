package vn.edu.tdtu.musicapplication.models.artist_request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EDocumentType;
import vn.edu.tdtu.musicapplication.utils.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;
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
    private String legalDocId;
    private String issuedBy;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dated;
    private String address;
    @ManyToOne
    @JoinColumn(name = "artistRequestId")
    @JsonIgnore
    private ArtistRequest artistRequest;
}