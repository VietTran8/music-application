package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.advertisement.AdStatistics;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdStatisticsRepository extends JpaRepository<AdStatistics, String> {
    @Query("SELECT a FROM AdStatistics a WHERE DATE(a.date) = ?1 AND a.advertisement = ?2")
    Optional<AdStatistics> findByDateAndAdvertisement(LocalDate date, Advertisement advertisement);
}
