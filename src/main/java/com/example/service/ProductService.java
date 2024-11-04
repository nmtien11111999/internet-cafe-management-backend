package com.example.service;

import com.example.model.Product;

import java.util.List;

public interface ProductService extends IService<Product>{
    List<Product> showAllFood();
    List<Product> showAllDrink();
}
