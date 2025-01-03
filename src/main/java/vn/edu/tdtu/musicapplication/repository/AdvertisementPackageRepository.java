package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.advertisement.AdvertisementPackage;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementPackageRepository extends JpaRepository<AdvertisementPackage, Long> {
    Optional<AdvertisementPackage> findByIdAndActive(long id, Boolean active);
    List<AdvertisementPackage> findByActive(Boolean active);
    Page<AdvertisementPackage> findByActive(Boolean active, Pageable pageable);
    Boolean existsByNameAndActive(String name, Boolean active);
}
