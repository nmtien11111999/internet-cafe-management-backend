package com.example.model.DTO;

import java.time.LocalDateTime;

public interface OrderAdminDTO {
    Long getId();
    String getProductName();
    Double getPrice();
    Integer getQuantity();
    Double getTotal();
    LocalDateTime getOrderDate();
    String getUserName();
}
