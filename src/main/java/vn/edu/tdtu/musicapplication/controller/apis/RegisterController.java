package vn.edu.tdtu.musicapplication.controller.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.RegisterUserRequest;
import vn.edu.tdtu.musicapplication.service.UserService;

@RestController
@RequestMapping("/api/register")
public record RegisterController(UserService userService) {
    @PostMapping
    public ResponseEntity<?> registerUser(
            @RequestBody RegisterUserRequest registerUserRequest
            ){
        BaseResponse<Object> response = userService.registerUser(registerUserRequest);
        return response.isStatus() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
