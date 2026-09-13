package com.example.shop.web;

public record Product (Long id,String name,Double price){
    public record PriceUpdate(double price) {}

}
