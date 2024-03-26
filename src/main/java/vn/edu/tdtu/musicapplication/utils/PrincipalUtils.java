package vn.edu.tdtu.musicapplication.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.repository.UserRepository;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class PrincipalUtils {
    private final UserRepository userRepository;
    public User loadUserFromPrincipal(Principal principal){
        String principalName = principal.getName();
        if(principalName.contains("@")){
            return userRepository.findByEmailAndActive(principal.getName(), true).orElse(null);
        }else{
            return userRepository.findByGoogleIdAndActive(principal.getName(), true).orElse(null);
        }
    }
}
