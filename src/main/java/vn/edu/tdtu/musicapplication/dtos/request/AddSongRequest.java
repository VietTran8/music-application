package vn.edu.tdtu.musicapplication.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.utils.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSongRequest {
    //YYYY-MM-DD
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime releaseDate;
    private Boolean isPremium;
    private String name;
    private String lyrics;
    private String audioUrl;
    private String imageUrl;
    private List<Long> artistIds;
    private Long albumId;
    private Long genreId;
}
