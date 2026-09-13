package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fakeProductRepository")
public class FakeProductRepository implements ProductRepository {

    private final List<Product> cung = List.of(
            new Product(101L, "Sản phẩm demo A", 10000d,5),
            new Product(102L, "Sản phẩm demo B", 20000d,10)
    );

    @Override public List<Product> findAll() { return cung; }

    @Override public Optional<Product> findById(Long id) {
        return cung.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    @Override public Product save(Product p) {
        throw new UnsupportedOperationException("Repository giả, chỉ để đọc");
    }

    @Override public boolean deleteById(Long id) { return false; }
}