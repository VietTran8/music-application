package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.enums.EArtistRequestStatus;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistRequest;

import java.util.List;

@Repository
public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Long> {
    Page<ArtistRequest> findByActive(Boolean active, Pageable pageable);
    List<ArtistRequest> findByActiveOrderByRequestedDateDesc(Boolean active);
    List<ArtistRequest> findByActiveAndStatus(Boolean active, EArtistRequestStatus status);
}
