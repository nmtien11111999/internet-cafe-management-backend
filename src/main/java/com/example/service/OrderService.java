package com.example.service;

import com.example.model.DTO.OrderDTO;
import com.example.model.Order;
import org.springframework.validation.BindingResult;

public interface OrderService {
    void addOrder(OrderDTO orderDTO);
}
