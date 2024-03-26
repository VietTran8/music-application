package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinimizedAlbum {
    private long id;
    private LocalDateTime releasedDate;
    private String description;
    private String title;
    private String imageUrl;
    private int noOfSongs;
    private String artist;
}
