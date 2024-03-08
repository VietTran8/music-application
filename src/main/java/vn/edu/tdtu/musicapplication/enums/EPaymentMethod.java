package vn.edu.tdtu.musicapplication.enums;

public enum EPaymentMethod {
    METHOD_VNPAY("Thanh toán chuyển khoản qua VNPAY");
    private final String description;

    EPaymentMethod(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
