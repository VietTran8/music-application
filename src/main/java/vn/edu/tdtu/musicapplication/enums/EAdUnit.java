package vn.edu.tdtu.musicapplication.enums;

import lombok.Data;

public enum EAdUnit {
    PER_MONTH("vnđ/tháng", 30), PER_7_DAYS("vnđ/7 ngày", 7), PER_3_DAYS("vnđ/3 ngày", 3);
    public String description;
    public Integer days;
     EAdUnit(String description, Integer days){
        this.description = description;
        this.days = days;
    }
}
