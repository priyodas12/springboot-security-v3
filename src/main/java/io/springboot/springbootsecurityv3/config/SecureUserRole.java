package io.springboot.springbootsecurityv3.config;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum SecureUserRole {

    DEVELOPER(Sets.newHashSet(SecurePermission.PRODUCT_READ,SecurePermission.PRODUCT_WRITE)),
    TESTER(Sets.newHashSet(SecurePermission.PRODUCT_READ,SecurePermission.PRODUCT_WRITE,SecurePermission.PRODUCT_MODIFY)),
    PRODUCT_OWNER(Sets.newHashSet(SecurePermission.PRODUCT_READ,SecurePermission.PRODUCT_WRITE,SecurePermission.PRODUCT_MODIFY,SecurePermission.PRODUCT_DELETE));

    private final Set<SecurePermission> permission;

    SecureUserRole(Set<SecurePermission> permission) {
        this.permission = permission;
    }

    public Set<SecurePermission> getPermission() {
        return permission;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority(){
        Set<SimpleGrantedAuthority> permissions=getPermission().stream().map(securePermission->new SimpleGrantedAuthority(securePermission.getPermission())).collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
