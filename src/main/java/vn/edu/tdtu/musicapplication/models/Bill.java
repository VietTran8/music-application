package vn.edu.tdtu.musicapplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.tdtu.musicapplication.enums.EProductType;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.models.advertisement.ContactInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime createdDate;
    private EProductType productType;
    private String paymentMethod;
    private String transactionId;
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userPackId")
    private UserPackageBought packageProduct;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adId")
    private Advertisement adProduct;

    //@param: bills: bill of current month, year
    public static Map<EProductType, Map<Integer, BigDecimal>> calculateWeeklyRevenueByProductType(List<Bill> bills, YearMonth yearMonth) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int totalWeeks = yearMonth.atEndOfMonth().get(weekFields.weekOfMonth());

        Map<EProductType, List<Bill>> billsByProductType = bills.stream()
                .collect(Collectors.groupingBy(Bill::getProductType));

        Map<EProductType, Map<Integer, BigDecimal>> revenueByWeekOfMonthAndProductType = new HashMap<>();

        for(EProductType type : EProductType.values()){
            Map<Integer, BigDecimal> revenueByWeek = IntStream.rangeClosed(1, totalWeeks)
                    .boxed()
                    .collect(Collectors.toMap(week -> week, week -> BigDecimal.ZERO));
            revenueByWeekOfMonthAndProductType.put(type, revenueByWeek);
        }

        billsByProductType.forEach((productType, billsList) -> {
            Map<Integer, BigDecimal> revenueByWeek = new HashMap<>();
            IntStream.rangeClosed(1, totalWeeks).forEach(week -> revenueByWeek.put(week, BigDecimal.ZERO));

            billsList.forEach(bill -> {
                int weekOfMonth = bill.getCreatedDate().get(weekFields.weekOfMonth());
                BigDecimal currentRevenue = revenueByWeek.getOrDefault(weekOfMonth, BigDecimal.ZERO);
                revenueByWeek.put(weekOfMonth, currentRevenue.add(bill.getAmount()));
            });

            revenueByWeekOfMonthAndProductType.put(productType, revenueByWeek);
        });

        return revenueByWeekOfMonthAndProductType;
    }
}
