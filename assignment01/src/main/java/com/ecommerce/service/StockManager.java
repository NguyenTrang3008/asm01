package com.ecommerce.service;

import com.ecommerce.repository.ProductRepository;

import java.util.List;

public class StockManager {
    private final ProductRepository productRepository;

    public StockManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void reserveStock(List<String> productIds) {
        for (String productId : productIds) {
            productRepository.findById(productId).ifPresent(product -> product.decreaseStock(1));
        }
    }

    public void releaseStock(List<String> productIds) {
        for (String productId : productIds) {
            productRepository.findById(productId).ifPresent(product -> product.increaseStock(1));
        }
    }
}
