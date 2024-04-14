package vn.edu.tdtu.musicapplication.enums;

public enum ERole {
    ROLE_USER("Người dùng"), ROLE_ARTIST("Nghệ sĩ"), ROLE_ADMIN("Quản trị viên");
    public final String description;

    ERole(String description){
        this.description = description;
    }
}
