package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.models.artist_request.PersonalInfo;

import java.util.List;

@Repository
public interface ArtistInfoRepository extends JpaRepository<ArtistInfo, Long> {
    Page<ArtistInfo> findByActive(Boolean active, Pageable pageable);
    List<ArtistInfo> findByArtistNameContainingIgnoreCase(String artistName);
    List<ArtistInfo> findAllByActive(Boolean active);
}
