package vn.edu.tdtu.musicapplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistRequest;
import vn.edu.tdtu.musicapplication.models.artist_request.PersonalInfo;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean active;
    private String email;
    private String avatar;
    private String password;
    private String username;

    //artistInfo = null -> is not an artist
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artistInfoId")
    private ArtistInfo artistInfo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<ArtistRequest> artistRequests;
    @OneToMany(mappedBy = "user")
    private List<Advertisement> advertisements;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Song",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "songId")
    )
    private List<Song> favouriteSongs;
    @ManyToMany
    @JoinTable(
            name = "User_Album",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "albumId")
    )
    private List<Album> favouriteAlbums;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserPackageBought> packages;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Playlist> playlists;
    @OneToMany(mappedBy = "user")
    private List<Bill> bills;
}
