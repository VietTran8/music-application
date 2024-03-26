package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByNameContainingIgnoreCase(String name);
    Page<Song> findByActive(Boolean active, Pageable pageable);
    List<Song> findByActive(Boolean active);
    List<Song> findByActiveAndGenre(Boolean active, Genre genre);
    List<Song> findByArtistInfoList(List<ArtistInfo> artistInfoList);
    Page<Song> findByActiveAndIsPremium(Boolean active, Boolean premium, Pageable pageable);
}
