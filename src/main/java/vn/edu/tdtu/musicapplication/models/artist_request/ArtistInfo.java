package vn.edu.tdtu.musicapplication.models.artist_request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.models.Album;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.models.Song;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ArtistInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String artistName;
    private String image;
    @ManyToMany
    @JoinTable(
            name = "ArtistInfo_Genre",
            joinColumns = @JoinColumn(name = "artistInfoId"),
            inverseJoinColumns = @JoinColumn(name = "genreId")
    )
    private List<Genre> genres;
    private Boolean active;

    //personal info = null -> artist was created by admin
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "personalInfoId")
    private PersonalInfo personalInfo;
    @ManyToMany(mappedBy = "artistInfoList")
    private List<Song> songs;
    @OneToMany(mappedBy = "artistInfo")
    private List<Album> albums;
}
