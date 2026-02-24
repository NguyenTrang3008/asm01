package com.ecommerce.service.discount;

import com.ecommerce.model.Product;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(Product product) {
        return product.getPrice();
    }
}
