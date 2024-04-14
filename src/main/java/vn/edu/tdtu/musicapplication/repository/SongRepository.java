package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("SELECT s FROM Song s JOIN s.artistInfoList a WHERE lower(s.name) LIKE lower(concat('%', :searchTerm, '%')) OR lower(a.artistName) LIKE lower(concat('%', :searchTerm, '%'))")
    List<Song> findBySongNameOrArtistName(@Param("searchTerm") String searchTerm);
    List<Song> findByNameContainingIgnoreCase(String name);
    Page<Song> findByActive(Boolean active, Pageable pageable);
    List<Song> findByActive(Boolean active);
    List<Song> findByActiveAndGenre(Boolean active, Genre genre);
    List<Song> findByArtistInfoList(List<ArtistInfo> artistInfoList);
    Page<Song> findByActiveAndIsPremium(Boolean active, Boolean premium, Pageable pageable);
    Long countAllByActive(Boolean active);
}
