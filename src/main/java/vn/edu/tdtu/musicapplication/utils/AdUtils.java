package vn.edu.tdtu.musicapplication.utils;

public class AdUtils {
    public static String convertTypeToVietnamese(String type) {
        return switch (type) {
            case "TYPE_NORMAL" -> "Bình thường";
            case "TYPE_PREMIUM" -> "Cao cấp";
            case "TYPE_MEDIUM" -> "Trung bình";
            default -> type;
        };
    }

    public static String convertUnitToVietnamese(String unit) {
        return switch (unit) {
            case "PER_MONTH" -> "30 ngày";
            case "PER_7_DAYS" -> "7 ngày";
            case "PER_3_DAYS" -> "3 ngày";
            default -> unit;
        };
    }
}
