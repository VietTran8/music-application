package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping("/upload/{type}")
    public ResponseEntity<?> uploadAvatar(Principal principal, @RequestParam("file") MultipartFile file, @PathVariable("type") String type){
        BaseResponse<?> response = userService.uploadUserImage(principal, file, type);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
