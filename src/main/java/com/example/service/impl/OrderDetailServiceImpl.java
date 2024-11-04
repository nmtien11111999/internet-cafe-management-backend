package com.example.service.impl;

import com.example.model.DTO.OrderAdminDTO;
import com.example.repository.OrderDetailRepository;
import com.example.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderAdminDTO> findOrderDetailsWithTotals() {
        return orderDetailRepository.findOrderDetailsWithTotals();
    }

    @Override
    @Transactional
    public void deleteOrderByOrderDetailId(Long orderDetailId) {
        orderDetailRepository.deleteOrderByOrderDetailId(orderDetailId);
    }

    @Override
    public List<OrderAdminDTO> getOrderDetailsByUsername(String username) {
        return orderDetailRepository.getOrderDetailsByUsername(username);
    }

    @Override
    public Double getTotalAmountByUsername(String username) {
        return orderDetailRepository.getTotalAmountByUsername(username);
    }


}
