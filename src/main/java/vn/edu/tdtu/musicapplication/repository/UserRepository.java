package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Role;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByEmailOrGoogleId(String email, String googleId);
    public Optional<User> findByArtistInfo(ArtistInfo artistInfo);
    public Optional<User> findByEmailAndActive(String email, Boolean active);
    public Optional<User> findByIdAndActive(Long id, Boolean active);
    public Optional<User> findByGoogleIdAndActive(String googleId, Boolean active);
    public Boolean existsByEmail(String email);
    public List<User> findByActive(Boolean active);
}
