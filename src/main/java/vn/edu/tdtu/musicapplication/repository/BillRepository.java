package vn.edu.tdtu.musicapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.tdtu.musicapplication.dtos.RevenueStatisticsDTO;
import vn.edu.tdtu.musicapplication.models.Bill;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, String> {
    @Query("SELECT b FROM Bill b WHERE YEAR(b.createdDate) = :year AND MONTH(b.createdDate) = :month")
    List<Bill> calculateWeeklyRevenueByProductTypeForCurrentMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT new vn.edu.tdtu.musicapplication.dtos.RevenueStatisticsDTO(b.productType, FUNCTION('MONTH', b.createdDate), SUM(b.amount)) " +
            "FROM Bill b " +
            "WHERE FUNCTION('YEAR', b.createdDate) = :year " +
            "GROUP BY b.productType, FUNCTION('MONTH', b.createdDate) " +
            "ORDER BY FUNCTION('MONTH', b.createdDate)")
    List<RevenueStatisticsDTO> calculateMonthlyRevenueByProductTypeForYear(@Param("year") int year);
}
