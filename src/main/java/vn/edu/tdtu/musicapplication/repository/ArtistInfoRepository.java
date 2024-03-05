package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.util.List;

@Repository
public interface ArtistInfoRepository extends JpaRepository<ArtistInfo, Long> {
    List<ArtistInfo> findByActive(Boolean active, Pageable pageable);
    List<ArtistInfo> findByArtistNameContainingIgnoreCase(String artistName);
}
