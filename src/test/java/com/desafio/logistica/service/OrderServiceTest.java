package com.desafio.logistica.service;

import com.desafio.logistica.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnTrueWhenOrderExists() {
        Integer orderId = 1;
        when(orderRepository.existsById(orderId)).thenReturn(true);

        boolean exists = orderService.existsById(orderId);

        assertTrue(exists);
        verify(orderRepository, times(1)).existsById(orderId);
    }

    @Test
    void shouldReturnFalseWhenOrderDoesNotExist() {
        Integer orderId = 2;
        when(orderRepository.existsById(orderId)).thenReturn(false);

        boolean exists = orderService.existsById(orderId);

        assertFalse(exists);
        verify(orderRepository, times(1)).existsById(orderId);
    }
}