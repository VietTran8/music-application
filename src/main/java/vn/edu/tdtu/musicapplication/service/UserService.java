package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.FollowUserRequest;
import vn.edu.tdtu.musicapplication.dtos.request.RegisterUserRequest;
import vn.edu.tdtu.musicapplication.dtos.response.FavouriteResponse;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.models.Follows;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.repository.FollowsRepository;
import vn.edu.tdtu.musicapplication.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public record UserService(
        RoleService roleService,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        FollowsRepository followsRepository
) {
    public Boolean userIsExistsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
    public User findByEmailAndActive(String email, Boolean active){
        return userRepository.findByEmailAndActive(email, true).orElse(null);
    }

    public BaseResponse<Object> registerUser(RegisterUserRequest registerUserRequest) {
        if(!userIsExistsByEmail(registerUserRequest.getEmail())){
            User newUser = User.builder()
                    .username(registerUserRequest.getUsername())
                    .email(registerUserRequest.getEmail())
                    .active(true)
                    .roles(List.of(roleService.findByName(ERole.ROLE_USER)))
                    .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                    .build();

            userRepository.save(newUser);
            return BaseResponse.builder()
                    .data(null)
                    .status(true)
                    .message("Đăng ký tài khoản thành công!")
                    .build();
        }
        return BaseResponse.builder()
                .data(null)
                .status(false)
                .message("Tài khoản đã tồn tại trong hệ thống!")
                .build();
    }

    public BaseResponse<?> followUser(Principal principal, FollowUserRequest request){
        BaseResponse<FavouriteResponse> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setMessage("You are not authenticated");
        response.setStatus(false);

        if(principal != null){
            User follower = findByEmail(principal.getName());
            User followed = findByEmail(request.getEmail());

            response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.setData(null);
            response.setMessage("User not found");
            response.setStatus(false);

            if(followed != null
                    && (followed.getActive() == null || followed.getActive())
                    && follower != null
                    && (follower.getActive() == null || follower.getActive())
            ){
                if(!Objects.equals(followed.getId(), follower.getId())){
                    List<Follows> follows = followsRepository.findByFollowerIdAndFollowedId(follower.getId(), followed.getId());
                    FavouriteResponse data = new FavouriteResponse();

                    boolean isFollowed = !follows.isEmpty();
                    log.info(String.valueOf(isFollowed));

                    if(isFollowed){
                        //unfollow
                        followsRepository.deleteAll(follows);
                        response.setMessage("Unfollowed user: " + followed.getEmail());
                        data.setStatus(false);
                    }else{
                        Follows newFollow = new Follows();
                        newFollow.setFollowedId(followed.getId());
                        newFollow.setFollowerId(follower.getId());
                        response.setMessage("Followed user: " + followed.getEmail());
                        data.setStatus(true);

                        followsRepository.save(newFollow);
                    }

                    data.setFavourites(followsRepository.findByFollowedId(followed.getId()).size());

                    response.setCode(HttpServletResponse.SC_ACCEPTED);
                    response.setData(data);
                    response.setStatus(true);
                }else{
                    response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
                    response.setData(null);
                    response.setStatus(false);
                    response.setMessage("You can not follow yourself");
                }
            }
        }

        return response;
    }
}
