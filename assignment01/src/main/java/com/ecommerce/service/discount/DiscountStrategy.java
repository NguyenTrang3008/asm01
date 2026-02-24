package com.ecommerce.service.discount;

import com.ecommerce.model.Product;

public interface DiscountStrategy {
    double applyDiscount(Product product);
}
