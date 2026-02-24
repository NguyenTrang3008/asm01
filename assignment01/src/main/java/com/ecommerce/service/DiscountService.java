package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.service.discount.ClothingDiscountStrategy;
import com.ecommerce.service.discount.DiscountStrategy;
import com.ecommerce.service.discount.ElectronicsDiscountStrategy;
import com.ecommerce.service.discount.NoDiscountStrategy;

import java.util.ArrayList;
import java.util.List;

public class DiscountService {
    private final List<DiscountStrategy> strategies;

    public DiscountService() {
        this.strategies = new ArrayList<>();
        strategies.add(new ElectronicsDiscountStrategy());
        strategies.add(new ClothingDiscountStrategy());
        strategies.add(new NoDiscountStrategy());
    }

    public double calculateDiscountedPrice(Product product) {
        for (DiscountStrategy strategy : strategies) {
            double discountedPrice = strategy.applyDiscount(product);
            if (discountedPrice != product.getPrice()) {
                return discountedPrice;
            }
        }
        return product.getPrice();
    }
}
