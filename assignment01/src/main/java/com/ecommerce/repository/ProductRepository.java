package com.ecommerce.repository;

import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    public ProductRepository() {
        // Initialize sample products
        products.add(new Product("P001", "Laptop", 999.99, 50, "ELECTRONICS"));
        products.add(new Product("P002", "T-Shirt", 29.99, 200, "CLOTHING"));
        products.add(new Product("P003", "Coffee Beans", 15.99, 100, "FOOD"));
        products.add(new Product("P004", "Headphones", 149.99, 75, "ELECTRONICS"));
    }

    public Optional<Product> findById(String productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();
    }
}
