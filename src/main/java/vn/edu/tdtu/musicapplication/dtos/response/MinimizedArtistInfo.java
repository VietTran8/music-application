package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinimizedArtistInfo {
    private long id;
    private String artistName;
    private String image;
    private boolean createdByAdmin;
}
