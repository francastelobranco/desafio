package com.desafio.logistica.dto;

import com.desafio.logistica.model.OrderEntity;
import com.desafio.logistica.model.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrderDtoTest {

    @Test
    void convertListToOrderDtoShouldMapFieldsCorrectly() {
        Date date = new Date();
        ProductEntity p1 = new ProductEntity(1, new BigDecimal("10.00"));
        ProductEntity p2 = new ProductEntity(2, new BigDecimal("20.00"));
        Set<ProductEntity> products = new HashSet<>(Arrays.asList(p1, p2));
        OrderEntity order = new OrderEntity(100, date, products);
        Set<OrderEntity> orders = new HashSet<>();
        orders.add(order);

        List<OrderDto> dtos = OrderDto.convertListToOrderDto(orders);

        assertEquals(1, dtos.size());
        OrderDto dto = dtos.get(0);
        assertEquals(100, dto.getOrderId());
        assertEquals(date, dto.getDate());
        assertEquals(BigDecimal.ZERO, dto.getTotal());

        assertNotNull(dto.getProducts());
        assertEquals(2, dto.getProducts().size());

        Set<Integer> ids = dto.getProducts().stream()
                .map(ProductDto::getProductId)
                .collect(Collectors.toSet());
        Set<BigDecimal> values = dto.getProducts().stream()
                .map(ProductDto::getValue)
                .collect(Collectors.toSet());

        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
        assertTrue(values.contains(new BigDecimal("10.00")));
        assertTrue(values.contains(new BigDecimal("20.00")));
    }

    @Test
    void convertListToOrderDtoEmptySetReturnsEmptyList() {
        Set<OrderEntity> empty = Collections.emptySet();
        List<OrderDto> dtos = OrderDto.convertListToOrderDto(empty);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}
