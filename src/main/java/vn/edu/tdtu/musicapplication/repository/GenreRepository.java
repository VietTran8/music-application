package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.musicapplication.models.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>{
    Boolean existsByNameAndActive(String name, Boolean active);
    List<Genre> findByActive(Boolean active);
}
