package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(0);

    @Override
    public List<Product> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Product save(Product product) {
        Long id = product.id() != null ? product.id() : idSeq.incrementAndGet();
        Product luu = new Product(id, product.name(), product.price(),product.stock());
        store.put(id, luu);
        return luu;
    }

    @Override
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }
}