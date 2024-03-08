package vn.edu.tdtu.musicapplication.dtos.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdApprovalRequest {
    private Long adId;
    private String reason;
    private String status; //PENDING - APPROVED - NOT_APPROVED
}
