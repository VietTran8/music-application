package vn.edu.tdtu.musicapplication.dtos.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistRequestApprovalRequest {
    private Long requestId;
    private String status;
    private String reason;
}
