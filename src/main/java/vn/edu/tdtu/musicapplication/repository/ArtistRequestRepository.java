package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistRequest;

@Repository
public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Long> {
    Page<ArtistRequest> findByActive(Boolean active, Pageable pageable);
}
