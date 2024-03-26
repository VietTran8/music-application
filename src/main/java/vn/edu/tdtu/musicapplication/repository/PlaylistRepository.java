package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Playlist;
import vn.edu.tdtu.musicapplication.models.User;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Boolean existsByTitleAndActiveAndUser(String title, Boolean active, User user);
    List<Playlist> findByActive(Boolean active);
    List<Playlist> findByActiveAndUserOrderByCreatedDateDesc(Boolean active, User user);
}
