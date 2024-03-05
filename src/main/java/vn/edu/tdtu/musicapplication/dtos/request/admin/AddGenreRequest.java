package vn.edu.tdtu.musicapplication.dtos.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddGenreRequest {
    private String name;
    private String description;
}
