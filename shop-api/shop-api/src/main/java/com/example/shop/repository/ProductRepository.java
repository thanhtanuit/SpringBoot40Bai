package com.example.shop.repository;

import com.example.shop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);   // id null -> tạo mới; id có sẵn -> ghi đè
    boolean deleteById(Long id);
}
