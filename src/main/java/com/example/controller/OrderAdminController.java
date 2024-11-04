package com.example.controller;

import com.example.model.DTO.OrderAdminDTO;
import com.example.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/order_admin")
public class OrderAdminController {
    @Autowired
    private OrderDetailService orderDetailService;
    @GetMapping(value = "/list")
    public ResponseEntity<List<OrderAdminDTO>> getOrderAdminList() {
        List<OrderAdminDTO> orderAdminList = orderDetailService.findOrderDetailsWithTotals();
        return ResponseEntity.ok(orderAdminList);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.deleteOrderByOrderDetailId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<OrderAdminDTO>> getOrderDetailsByUsername(@RequestParam String username) {
        List<OrderAdminDTO> orderDetailsByUsername = orderDetailService.getOrderDetailsByUsername(username);
        return ResponseEntity.ok(orderDetailsByUsername);
    }

    @GetMapping("/users/{username}/totalAmount")
    public ResponseEntity<Double> getTotalAmountByUsername(@PathVariable String username) {
        Double total = orderDetailService.getTotalAmountByUsername(username);
        return ResponseEntity.ok(total);
    }
}
