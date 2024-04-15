package vn.edu.tdtu.musicapplication.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.tdtu.musicapplication.enums.EPackageType;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistRequest;
import vn.edu.tdtu.musicapplication.models.artist_request.PersonalInfo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean active;
    private String email;
    private String avatar;
    private String googleId;
    private String headerImg;
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

    public boolean getIsPremium(){
        return this.getPackages().stream().anyMatch(UserPackageBought::isNotExpired);
    };

    public EPackageType getAccountType(){
        return this.getPackages().stream()
                .filter(UserPackageBought::isNotExpired)
                .map(p -> p.getMPackage().getType())
                .reduce((type1, type2) -> {
                    if (type1 == EPackageType.TYPE_PREMIUM || type2 == EPackageType.TYPE_PREMIUM) {
                        return EPackageType.TYPE_PREMIUM;
                    } else {
                        return type1;
                    }
                })
                .orElse(null);
    }

    public String getRoleString(){
        return this.getRoles().stream().map(
                r -> r.getName().description
        ).collect(Collectors.joining(" - "));
    }
}
