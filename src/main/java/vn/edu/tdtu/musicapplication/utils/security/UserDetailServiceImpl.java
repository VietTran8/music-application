package vn.edu.tdtu.musicapplication.utils.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with email %s", username)));
        return new UserDetailImpl(foundUser);
    }
}
