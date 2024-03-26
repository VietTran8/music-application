package vn.edu.tdtu.musicapplication.dtos;

import vn.edu.tdtu.musicapplication.enums.EProductType;

import java.math.BigDecimal;

public class RevenueStatisticsDTO {
    private EProductType productType;
    private int timeUnit; // Tháng hoặc tuần tùy thuộc vào truy vấn
    private int year; // Chỉ sử dụng cho truy vấn hàng tháng
    private BigDecimal totalRevenue;

    // Constructor cho truy vấn hàng tháng
    public RevenueStatisticsDTO(EProductType productType, int month, int year, BigDecimal totalRevenue) {
        this.productType = productType;
        this.timeUnit = month;
        this.year = year;
        this.totalRevenue = totalRevenue;
    }

    // Constructor cho truy vấn hàng tuần
    public RevenueStatisticsDTO(EProductType productType, int week, BigDecimal totalRevenue) {
        this.productType = productType;
        this.timeUnit = week;
        this.totalRevenue = totalRevenue;
    }

    // Getters và Setters
    public EProductType getProductType() {
        return productType;
    }

    public void setProductType(EProductType productType) {
        this.productType = productType;
    }

    public int getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(int timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}

