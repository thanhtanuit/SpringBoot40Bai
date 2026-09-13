package com.example.shop.web;

import com.example.shop.dto.CreateProductRequest;
import com.example.shop.dto.PriceUpdateRequest;
import com.example.shop.dto.ProductResponse;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.model.PriceUpdate;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return ProductMapper.toResponseList(service.findAll());
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return ProductMapper.toResponse(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        var moi = service.create(req.name(), req.price(), req.stock());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(moi.id()).toUri();
        return ResponseEntity.created(location).body(ProductMapper.toResponse(moi));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody CreateProductRequest req) {
        return ProductMapper.toResponse(service.update(id, req.name(), req.price(), req.stock()));
    }

    @PatchMapping("/{id}/price")
    public ProductResponse updatePrice(@PathVariable Long id, @Valid @RequestBody PriceUpdateRequest req) {
        return ProductMapper.toResponse(service.updatePrice(id, req.price()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
