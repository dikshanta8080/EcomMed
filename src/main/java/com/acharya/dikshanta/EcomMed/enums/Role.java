package com.acharya.dikshanta.EcomMed.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(
            Permission.VENDOR_CREATE,
            Permission.VENDOR_UPDATE,
            Permission.VENDOR_DELETE,
            Permission.PRODUCT_GET,
            Permission.ORDER_PLACE,
            Permission.CART_UPDATE,
            Permission.CART_ADD,
            Permission.CART_DELETE,
            Permission.USERS_GET
    )),
    VENDOR(Set.of(
            Permission.PRODUCT_ADD,
            Permission.PRODUCT_UPDATE,
            Permission.PRODUCT_GET,
            Permission.CART_DELETE

    )),

    CUSTOMER(Set.of(
            Permission.PRODUCT_GET,
            Permission.ORDER_PLACE,
            Permission.CART_UPDATE,
            Permission.CART_ADD,
            Permission.CART_DELETE));

    @Getter
    private final Set<Permission> permissions;

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = permissions
                .stream()
                .map(permission ->
                        new SimpleGrantedAuthority(permission.getPermissionName()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
