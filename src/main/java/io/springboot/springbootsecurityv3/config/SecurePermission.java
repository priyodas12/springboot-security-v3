package io.springboot.springbootsecurityv3.config;

public enum SecurePermission {

    PRODUCT_READ("PRODUCT:READ"),
    PRODUCT_WRITE("PRODUCT:WRITE"),
    PRODUCT_DELETE("PRODUCT:DELETE"),
    PRODUCT_MODIFY("PRODUCT:UPDATE");


    private final String permission;

    SecurePermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }


}
