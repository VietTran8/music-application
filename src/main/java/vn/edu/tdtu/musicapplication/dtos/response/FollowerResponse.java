package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.models.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowerResponse {
    private User user;
    private String redirectUrl;
    private boolean isArtist;
}
