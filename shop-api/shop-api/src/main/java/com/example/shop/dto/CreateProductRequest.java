package com.example.shop.dto;

import jakarta.validation.constraints.*;

public record CreateProductRequest(

        @NotBlank(message = "Tên sản phẩm không được để trống")
        @Size(min = 2, max = 100, message = "Tên sản phẩm phải từ {min} đến {max} ký tự")
        String name,

        @NotNull(message = "Giá không được để trống")
        @Positive(message = "Giá phải lớn hơn 0")
        Double price,

        @Min(value = 0, message = "Tồn kho không được âm")
        Integer stock

) {}