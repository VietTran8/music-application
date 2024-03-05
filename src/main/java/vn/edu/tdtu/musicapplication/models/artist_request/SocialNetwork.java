package vn.edu.tdtu.musicapplication.models.artist_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.ESocialNetwork;
import vn.edu.tdtu.musicapplication.models.artist_request.PersonalInfo;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SocialNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private ESocialNetwork type;
    private String link;
    @ManyToOne
    @JoinColumn(name = "personalInfoId")
    @JsonIgnore
    private PersonalInfo personalInfo;
}
