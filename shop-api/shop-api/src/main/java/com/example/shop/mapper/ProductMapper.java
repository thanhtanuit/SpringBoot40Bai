package com.example.shop.mapper;

import com.example.shop.dto.ProductResponse;
import com.example.shop.model.Product;

import java.util.List;

public final class ProductMapper {

    private ProductMapper() {}

    public static ProductResponse toResponse(Product p) {
        return new ProductResponse(p.id(), p.name(), p.price(), p.stock());
    }

    public static List<ProductResponse> toResponseList(List<Product> list) {
        return list.stream().map(ProductMapper::toResponse).toList();
    }
}