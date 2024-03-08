package vn.edu.tdtu.musicapplication.enums;

public enum EProductType {
    ADS("Quảng cáo"), UPGRADE_PACKAGE("Gói nâng cấp tài khoản");

    private final String description;
    EProductType(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
