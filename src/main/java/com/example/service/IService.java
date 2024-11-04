package com.example.service;

import org.springframework.validation.BindingResult;

import java.util.List;

public interface IService<T> {
    T save(T t, BindingResult bindingResult);
    T update(T t,Long id,BindingResult bindingResult);
    T findById(Long id);
    T delete(Long id);
    List<T> findAll();
    List<T> findByName(String name);
}
