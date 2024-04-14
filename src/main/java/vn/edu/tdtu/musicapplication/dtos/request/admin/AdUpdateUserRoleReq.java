package vn.edu.tdtu.musicapplication.dtos.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.ERole;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUpdateUserRoleReq {
    private Long userId;
    private List<ERole> roles;
}
