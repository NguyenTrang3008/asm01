package com.ecommerce.service.discount;

import com.ecommerce.model.Product;

public class ClothingDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(Product product) {
        if (product.getCategory().equals("CLOTHING")) {
            return product.getPrice() * 0.90; // 10% off all clothing
        }
        return product.getPrice();
    }
}
