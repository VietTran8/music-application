package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.FollowUserRequest;
import vn.edu.tdtu.musicapplication.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/follow")
    public ResponseEntity<?> followUser(Principal principal, @RequestBody FollowUserRequest requestBody){
        BaseResponse<?> response = userService.followUser(principal, requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
