package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.FollowUserRequest;
import vn.edu.tdtu.musicapplication.dtos.request.RegisterUserRequest;
import vn.edu.tdtu.musicapplication.dtos.response.FavouriteResponse;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.enums.EUploadFolder;
import vn.edu.tdtu.musicapplication.models.Follows;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.repository.FollowsRepository;
import vn.edu.tdtu.musicapplication.repository.UserRepository;
import vn.edu.tdtu.musicapplication.service.cloudinary.IFileService;
import vn.edu.tdtu.musicapplication.utils.PrincipalUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public record UserService(
        RoleService roleService,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        FollowsRepository followsRepository,
        IFileService fileService,
        PrincipalUtils principalUtils
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
    public User findByGoogleId(String googleId){
        return userRepository.findByGoogleIdAndActive(googleId, true).orElse(null);
    }

    public User findByArtistInfo(ArtistInfo artistInfo){
        return userRepository.findByArtistInfo(artistInfo).orElse(null);
    }

    public User findByEmailAndActive(String email, Boolean active){
        return userRepository.findByEmailAndActive(email, true).orElse(null);
    }

    public BaseResponse<Object> registerUser(RegisterUserRequest registerUserRequest) {
        if(!userIsExistsByEmail(registerUserRequest.getEmail())){
            User newUser = User.builder()
                    .username(registerUserRequest.getUsername())
                    .email(registerUserRequest.getEmail())
                    .avatar(registerUserRequest.getImgUrl())
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

    public List<User> getFollowersByUserId(Long userId)
    {
        User foundUser = userRepository.findByIdAndActive(userId, true).orElse(null);
        if(foundUser != null){
            List<User> followers = new ArrayList<>();
            long[] followerIds = followsRepository.findByFollowedId(foundUser.getId()).stream().mapToLong(Follows::getFollowerId).toArray();
            for(long id : followerIds)
            {
                userRepository.findByIdAndActive(id, true).ifPresent(followers::add);
            }

            return followers;
        }
        return new ArrayList<>();
    }

    public BaseResponse<?> followUser(Principal principal, FollowUserRequest request){
        BaseResponse<FavouriteResponse> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setMessage("Vui lòng đăng nhập");
        response.setStatus(false);

        if(principal != null){
            User follower = principalUtils.loadUserFromPrincipal(principal);
            User followed = findByEmail(request.getEmail());

            response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.setData(null);
            response.setMessage("Không tìm thấy người dùng");
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
                        response.setMessage("Đã hủy theo dõi");
                        data.setStatus(false);
                    }else{
                        Follows newFollow = new Follows();
                        newFollow.setFollowedId(followed.getId());
                        newFollow.setFollowerId(follower.getId());
                        response.setMessage("Đã theo dõi");
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
                    response.setMessage("Bạn không thể theo dõi chính bạn");
                }
            }
        }

        return response;
    }

    public BaseResponse<?> uploadUserImage(Principal principal, MultipartFile file, String type){
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        if(principal != null){
            User user = findByEmail(principal.getName());
            try{
                String url = fileService.uploadFile(file, EUploadFolder.FOLDER_IMG);
                if(type.equals("avatar"))
                    user.setAvatar(url);
                else
                    user.setHeaderImg(url);

                saveUser(user);

                Map<String, Object> data = new HashMap<>();
                data.put("url", url);

                response.setMessage("Image updated successfully!");
                response.setData(data);
                response.setStatus(true);
                response.setCode(HttpServletResponse.SC_OK);

                return response;
            }catch (IOException ex){
                log.error(ex.getMessage());
                response.setMessage("Something went wrong: " + ex.getMessage());
            }
            response.setData(null);
            response.setStatus(false);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);

            return response;
        }

        response.setMessage("You are not authenticated!");
        response.setData(null);
        response.setStatus(false);
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);

        return response;
    }
}
