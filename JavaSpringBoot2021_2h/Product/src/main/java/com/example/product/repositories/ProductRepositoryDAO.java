package com.example.product.repositories;

import com.example.product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepositoryDAO extends JpaRepository<Product,Long> {
    List<Product> findByProductName(String productName);
}
