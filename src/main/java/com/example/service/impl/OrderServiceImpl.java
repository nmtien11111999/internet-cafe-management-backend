package com.example.service.impl;

import com.example.model.DTO.OrderDTO;
import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import com.example.model.OrderDetail;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.OrderDetailRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public void addOrder(OrderDTO orderDTO) {
        Optional<Product> productOptional = productRepository.findById(orderDTO.getProductId());
        Optional<User> userOptional = userRepository.findById(orderDTO.getId());

        if (productOptional.isPresent() && userOptional.isPresent()) {
            User user = userOptional.get();
            Product product = productOptional.get();



                Order order = new Order();
                order.setUser(user);
                order.setOrderDate(LocalDateTime.now());
                orderRepository.save(order);

                OrderDetail orderDetailNew = new OrderDetail();
                orderDetailNew.setProduct(product);
                orderDetailNew.setOrder(order);
                orderDetailNew.setQuantity(orderDTO.getQuantity());
                orderDetailNew.setTotal(product.getPrice() * orderDTO.getQuantity());
                orderDetailRepository.save(orderDetailNew);

        }
    }
}

