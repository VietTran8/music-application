package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Follows;
import vn.edu.tdtu.musicapplication.models.User;

import java.util.List;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, Long> {
    List<Follows> findByFollowerIdAndFollowedId(Long follower, Long followed);
    List<Follows> findByFollowedId(Long followed);
    List<Follows> findByFollowerId(Long follower);
}