package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.enums.EAdType;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    @Query("SELECT a FROM Advertisement a WHERE a.active = true AND a.expirationDate > ?1 AND a.status = 'APPROVED' AND a.aPackage.type = ?2")
    Page<Advertisement> findActiveAndApprovedAdvertisementsWithExpirationAfter(LocalDateTime now, EAdType type, Pageable pageable);
}
