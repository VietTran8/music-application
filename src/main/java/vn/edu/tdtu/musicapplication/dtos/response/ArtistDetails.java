package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.models.artist_request.PersonalInfo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDetails {
    private long id;
    private String artistName;
    private String image;
    private PersonalInfo personalInfo;
    private List<MinimizedAlbum> albums;
    private List<MinimizedSong> songs;
    private List<MinimizedGenre> genres;
}
