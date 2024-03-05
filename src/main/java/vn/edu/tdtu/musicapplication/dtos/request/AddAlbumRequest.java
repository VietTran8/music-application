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
@NoArgsConstructor
@AllArgsConstructor
public class AddAlbumRequest {
    //YYYY-MM-DD
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime releasedDate;
    private String description;
    private String title;
    private String imageUrl;
    private Long artistId;
    private List<Long> songIds;
}
