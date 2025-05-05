package com.desafio.logistica.service;


import com.desafio.logistica.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public boolean existsById(Integer orderId) {
        return repository.existsById(orderId);
    }
}
