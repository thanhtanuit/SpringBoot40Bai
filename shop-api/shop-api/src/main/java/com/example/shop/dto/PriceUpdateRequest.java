package com.example.shop.dto;

import jakarta.validation.constraints.*;

public record PriceUpdateRequest(
        @NotNull(message = "Giá không được để trống")
        @Positive(message = "Giá phải lớn hơn 0")
        Double price
) {}