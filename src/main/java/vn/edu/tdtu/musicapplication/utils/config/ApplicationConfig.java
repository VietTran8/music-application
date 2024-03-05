package vn.edu.tdtu.musicapplication.utils.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.models.Role;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.service.RoleService;
import vn.edu.tdtu.musicapplication.service.UserService;

import java.util.List;

@Configuration
public class ApplicationConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Bean
    public CommandLineRunner runner(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if(!roleService.roleExistsByName(ERole.ROLE_ADMIN)){
                    roleService.saveAllRole(
                            List.of(
                                    Role.builder().name(ERole.ROLE_ADMIN).build(),
                                    Role.builder().name(ERole.ROLE_ARTIST).build(),
                                    Role.builder().name(ERole.ROLE_USER).build()
                            )
                    );
                }
                if(!userService.userIsExistsByEmail("hoanviet882003@gmail.com")){
                    User user = User.builder()
                            .email("hoanviet882003@gmail.com")
                            .password("$2a$12$BfBkPcoR1ZDSEg0ujDnGte3ADqbFDHZUlgZbkFiDSnu1AUcf6CFh.")
                            .active(true)
                            .roles(List.of(roleService.findByName(ERole.ROLE_USER)))
                            .build();
                    userService.saveUser(user);
                }
            }
        };
    }
}
