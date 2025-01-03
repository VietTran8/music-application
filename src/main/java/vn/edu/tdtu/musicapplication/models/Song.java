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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Boolean active;
    private LocalDateTime releaseDate;
    private Boolean isPremium;
    private String name;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String lyrics;
    private String audioUrl;
    private String imageUrl;
    private int plays;
    private String author;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Song_Artist",
            joinColumns = @JoinColumn(name = "songId"),
            inverseJoinColumns = @JoinColumn(name = "artistInfoId")
    )
    private List<ArtistInfo> artistInfoList;
    @ManyToMany(mappedBy = "favouriteSongs", fetch = FetchType.EAGER)
    private List<User> users;
    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists;
    @ManyToOne
    @JoinColumn(name = "albumId")
    private Album album;
    @ManyToOne
    @JoinColumn(name = "genreId")
    private Genre genre;
    public int getFavoritesCount(){
        return this.getUsers() != null ? this.getUsers().size() : 0;
    }
}
