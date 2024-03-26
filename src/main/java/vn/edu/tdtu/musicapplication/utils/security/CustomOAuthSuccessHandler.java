package vn.edu.tdtu.musicapplication.utils.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.RegisterUserRequest;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.repository.UserRepository;
import vn.edu.tdtu.musicapplication.service.RoleService;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final RoleService roleService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication.getPrincipal() instanceof DefaultOAuth2User userDetails) {
            String email = userDetails.getAttribute("email");
            String imgUrl = userDetails.getAttribute("picture");
            String fullName = userDetails.getAttribute("name");
            if(!userRepository.existsByEmail(email)){
                RegisterUserRequest registerUserRequest = new RegisterUserRequest();
                registerUserRequest.setEmail(email);
                registerUserRequest.setPassword("default-password");
                registerUserRequest.setUsername(fullName);
                registerUserRequest.setImgUrl(imgUrl);

                User newUser = User.builder()
                        .username(registerUserRequest.getUsername())
                        .email(registerUserRequest.getEmail())
                        .avatar(registerUserRequest.getImgUrl())
                        .active(true)
                        .roles(List.of(roleService.findByName(ERole.ROLE_USER)))
                        .password(new BCryptPasswordEncoder().encode(registerUserRequest.getPassword()))
                        .googleId(((DefaultOAuth2User) authentication.getPrincipal()).getName())
                        .build();

                userRepository.save(newUser);
            }

            log.info(((DefaultOAuth2User) authentication.getPrincipal()).getName());

            response.sendRedirect("/home");
        }
    }
}
