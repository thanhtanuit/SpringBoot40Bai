package com.example.shop.service;

import com.example.shop.exception.*;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", id));
    }

    public Product create(String name, double price, Integer stock) {
        kiemTraTrungTen(name, null);
        return repo.save(new Product(null, name, price, stock == null ? 0 : stock));
    }

    public Product update(Long id, String name, double price, Integer stock) {
        findById(id);                       // ném 404 nếu không có
        kiemTraTrungTen(name, id);
        return repo.save(new Product(id, name, price, stock == null ? 0 : stock));
    }

    public Product updatePrice(Long id, double price) {
        Product cu = findById(id);
        return repo.save(new Product(id, cu.name(), price, cu.stock()));
    }

    public void delete(Long id) {
        findById(id);
        repo.deleteById(id);
    }

    private void kiemTraTrungTen(String name, Long idDangSua) {
        boolean trung = repo.findAll().stream()
                .anyMatch(p -> p.name().equalsIgnoreCase(name)
                        && !p.id().equals(idDangSua));
        if (trung) {
            throw new DuplicateResourceException("Đã tồn tại sản phẩm tên: " + name);
        }
    }
}