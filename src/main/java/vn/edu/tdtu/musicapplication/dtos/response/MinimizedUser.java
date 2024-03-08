package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinimizedUser {
    private Long id;
    private String email;
    private String avatar;
    private String username;
}
