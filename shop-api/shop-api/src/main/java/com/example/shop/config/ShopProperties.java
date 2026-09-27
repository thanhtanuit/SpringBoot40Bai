package com.example.shop.config;

import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "shop")
public record ShopProperties(

        @NotBlank
        String name,

        @Min(1) @Max(100)
        int maxItemsPerPage,

        @NotBlank
        String currency
) {}