package vn.edu.tdtu.musicapplication.dtos;

import lombok.Getter;
import lombok.Setter;
import vn.edu.tdtu.musicapplication.enums.EProductType;

import java.math.BigDecimal;

@Setter
@Getter
public class RevenueStatisticsDTO {
    private EProductType productType;
    private int timeUnit;
    private int year;
    private BigDecimal totalRevenue;

    public RevenueStatisticsDTO(EProductType productType, int month, int year, BigDecimal totalRevenue) {
        this.productType = productType;
        this.timeUnit = month;
        this.year = year;
        this.totalRevenue = totalRevenue;
    }

    public RevenueStatisticsDTO(EProductType productType, int week, BigDecimal totalRevenue) {
        this.productType = productType;
        this.timeUnit = week;
        this.totalRevenue = totalRevenue;
    }
}

