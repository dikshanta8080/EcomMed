package com.acharya.dikshanta.EcomMed.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
    // ADMIN - VENDOR MANAGEMENT
    VENDOR_CREATE("vendor:create"),
    VENDOR_UPDATE("vendor:update"),
    VENDOR_DELETE("vendor:delete"),

    // VENDOR - PRODUCT MANAGEMENT
    PRODUCT_ADD("product:add"),
    PRODUCT_UPDATE("product:update"),
    PRODUCT_DELETE("product:delete"),
    PRODUCT_GET("product:get"),

    USERS_GET("users:get"),

    // CATEGORY MANAGEMENT
    CATEGORY_ADD("category:add"),
    CATEGORY_DELETE("category:delete"),
    CATEGORY_GET("category:get"),

    // CUSTOMER - CART & ORDER
    ORDER_PLACE("order:place"),
    CART_ADD("cart:add"),
    CART_UPDATE("cart:update"),
    CART_DELETE("cart:delete"),

    // INVENTORY
    INVENTORY_UPDATE("inventory:update"),
    INVENTORY_DELETE("inventory:delete");
    private final String permissionName;


}
