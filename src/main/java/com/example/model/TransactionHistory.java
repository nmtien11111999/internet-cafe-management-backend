package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private Double price;
    private LocalDateTime dateTime;
    @PrePersist
    public void orderDate(){
        this.dateTime = LocalDateTime.now();
    }
}
