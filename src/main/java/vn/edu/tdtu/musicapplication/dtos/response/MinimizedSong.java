package vn.edu.tdtu.musicapplication.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinimizedSong {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime releaseDate;
    private Boolean isPremium;
    private String name;
    private String lyrics;
    private String audioUrl;
    private String imageUrl;
    private int favourites;
    private MinimizedGenre genre;
    private MinimizedAlbum album;
    private List<MinimizedArtistInfo> artists;
    private String author;
}
