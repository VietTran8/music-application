package vn.edu.tdtu.musicapplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Boolean active;
    private LocalDateTime releasedDate;
    private String description;
    private String title;
    private String imageUrl;
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Song> songs;
    @ManyToMany(mappedBy = "favouriteAlbums")
    private List<User> users;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artistInfoId")
    private ArtistInfo artistInfo;
}
