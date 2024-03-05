package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Package;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    Boolean existsByNameAndActive(String name, Boolean active);
    List<Package> findByActive(Boolean active);
    Optional<Package> findByIdAndActive(Long id, Boolean active);
}
