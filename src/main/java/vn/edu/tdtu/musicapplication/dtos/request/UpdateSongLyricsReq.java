package vn.edu.tdtu.musicapplication.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSongLyricsReq {
    private Long songId;
    private String newSongLyrics;
}
