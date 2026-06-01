package com.acharya.dikshanta.EcomMed.constrants;

public interface MessageConstants {

    interface UserConstants {
        String USER_ALREADY_EXISTS = "User Already Exists";
    }

    interface AuthConstants {
        String REGISTRATION_SUCCESSFUL = "Registration Successful";
        String LOGIN_SUCCESSFUL = "Login Successful";
    }

    interface ProductConstants {
        String PRODUCT_EXISTS = "Product Already Exists";
        String INVALID_QUANTITY = "Product Quantity must be greater than 0";
        String PRODUCT_NOT_FOUND = "Product Not Found";
        String PRODUCT_CREATED = "Product Created";
    }

    interface CategoryConstants {
        String CATEGORY_NOT_FOUND = "Category Not Found";
        String CATEGORY_ALREADY_EXISTS = "Category Already Exists";
        String CATEGORY_FETCHED = "Categories fetched successfully";
        String CATEGORY_ADDED = "Categories added successfully";
    }

    interface InventoryConstants {
        String INVENTORY_NOT_FOUND = "The item is not in stock";
        String INVALID_QUANTITY = "Please provide valid quantity";
    }

}
