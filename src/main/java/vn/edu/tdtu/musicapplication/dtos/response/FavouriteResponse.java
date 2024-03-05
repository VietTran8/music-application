package vn.edu.tdtu.musicapplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavouriteResponse {
    private Integer favourites;
    //True: followed / favourite
    //False: unfollowed / cancel favourite
    private Boolean status;
}
