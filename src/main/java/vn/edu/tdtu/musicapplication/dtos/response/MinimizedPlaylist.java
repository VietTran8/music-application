package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinimizedPlaylist {
    private Long id;
    private String title;
    private LocalDateTime createdDate;
    private List<MinimizedSong> songs;
    private List<String> thumbnails;
    private int noOfThumbs;
}
