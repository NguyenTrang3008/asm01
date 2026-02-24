package com.ecommerce.service.discount;

import com.ecommerce.model.Product;

public class ElectronicsDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(Product product) {
        if (product.getCategory().equals("ELECTRONICS") && product.getPrice() > 500) {
            return product.getPrice() * 0.95; // 5% off expensive electronics
        }
        return product.getPrice();
    }
}
