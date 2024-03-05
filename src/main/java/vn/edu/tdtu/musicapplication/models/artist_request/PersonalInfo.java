package vn.edu.tdtu.musicapplication.models.artist_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PersonalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    private LocalDateTime birthDate;
    private String nation;
    private String phoneNumber;
    private String bankAccountName;
    private String bankAccountNumber;
    @OneToOne(mappedBy = "personalInfo")
    @JsonIgnore
    private ArtistInfo artistInfo;
    @OneToOne(mappedBy = "personalInfo")
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "personalInfo")
    private List<SocialNetwork> socialNetworks;
}
