package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.UserPackageBought;

import java.util.List;

@Repository
public interface UserPackageBoughtRepository extends JpaRepository<UserPackageBought, Long> {
    List<UserPackageBought> findByUserAndStatus(User user, Boolean status);
}
