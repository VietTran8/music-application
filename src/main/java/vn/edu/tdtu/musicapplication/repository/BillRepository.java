package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.tdtu.musicapplication.models.Bill;

public interface BillRepository extends JpaRepository<Bill, String> {
}
