package com.example.service.impl;

import com.example.controller.ExceptionController;
import com.example.model.Category;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public Product save(Product product, BindingResult bindingResult) {
        List<String> errors = ExceptionController.getMessageError(bindingResult);
        if (productRepository.existsProductByName(product.getName())) {
            errors.add("name: Tên đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product, Long id, BindingResult bindingResult) {
        Product productById = findById(id);
        List<String> errors = ExceptionController.getMessageError(bindingResult);
        if (!product.getName().equals(productById.getName())){
            if (productRepository.existsProductByName(product.getName())) {
                errors.add("name: Tên đã tồn tại");
            }
            if (errors.size() > 0) {
                throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
            }
        }
        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return productRepository.findById(id).get();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Product delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        if (productRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByName(String name) {
        if (productRepository.findByNameContaining(name).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return productRepository.findByNameContaining(name);
    }

    @Override
    public List<Product> showAllFood() {
        return productRepository.findAllByCategoryName("đồ ăn");
    }

    @Override
    public List<Product> showAllDrink() {
        return productRepository.findAllByCategoryName("đồ uống");
    }

}
