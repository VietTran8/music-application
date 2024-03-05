package vn.edu.tdtu.musicapplication.service;

import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.models.Role;
import vn.edu.tdtu.musicapplication.repository.RoleRepository;

import java.util.List;

@Service
public record RoleService(RoleRepository roleRepository) {
    public Boolean roleExistsByName(ERole name){
        return roleRepository.existsByName(name);
    }
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    public Role findByName(ERole name){
        return roleRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("Role not found with name: " + name.name()));
    }

    public List<Role> saveAllRole(List<Role> roles){
        return roleRepository.saveAll(roles);
    }
}
