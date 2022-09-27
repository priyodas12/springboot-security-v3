package io.springboot.springbootsecurityv3.config;

public enum SecurePermission {

    PRODUCT_READ("PRODUCT_READ"),
    PRODUCT_WRITE("PRODUCT_WRITE"),
    PRODUCT_DELETE("PRODUCT_DELETE"),
    PRODUCT_MODIFY("PRODUCT_UPDATE");


    private final String permission;

    SecurePermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }


}
