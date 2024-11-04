package com.example.service.impl;

import com.example.controller.ExceptionController;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category, BindingResult bindingResult) {
        List<String> errors = ExceptionController.getMessageError(bindingResult);
        if (categoryRepository.existsCategoryByName(category.getName())) {
            errors.add("name: Tên đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category, Long id, BindingResult bindingResult) {
        Category oldCategory = findById(id);
        List<String> errors = ExceptionController.getMessageError(bindingResult);
        if (categoryRepository.existsCategoryByName(category.getName())) {
            errors.add("name: Tên đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new ValidationException(errors.stream().collect(Collectors.joining("; ")));
        }
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return categoryRepository.findById(id).get();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Category delete(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
        return category;
    }

    @Override
    public List<Category> findAll() {
        if (categoryRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByName(String name) {
        if (categoryRepository.findByNameContaining(name).isEmpty()) {
            throw new IllegalArgumentException();
        }
        return categoryRepository.findByNameContaining(name);
    }
}
