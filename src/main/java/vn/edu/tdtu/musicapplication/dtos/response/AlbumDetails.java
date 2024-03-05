package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDetails {
    private long id;
    private LocalDateTime releasedDate;
    private String description;
    private String title;
    private String imageUrl;
    private MinimizedArtistInfo artist;
    private List<MinimizedSong> songs;
}