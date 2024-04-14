package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Album;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByActiveAndArtistInfo(Boolean active, ArtistInfo artistInfo);
    List<Album> findByTitleContainingIgnoreCaseOrArtistInfoArtistNameContainingIgnoreCase(String title, String artistInfo_artistName);
    Page<Album> findByActive(Boolean active, Pageable pageable);
    List<Album> findByActive(Boolean active);
    Long countAllByActive(Boolean active);
}
