package com.desafio.logistica.dto;

import com.desafio.logistica.model.OrderEntity;
import com.desafio.logistica.model.ProductEntity;
import com.desafio.logistica.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void convertListToUserDtoWithNoFilterMapsAllOrders() {
        Date date = new Date();
        OrderEntity o1 = new OrderEntity(1, date, new HashSet<>(Collections.singletonList(new ProductEntity(10, new BigDecimal("1.00")))));
        OrderEntity o2 = new OrderEntity(2, date, new HashSet<>(Collections.singletonList(new ProductEntity(20, new BigDecimal("2.00")))));
        UserEntity u = new UserEntity(100, "Alice", new HashSet<>(Arrays.asList(o1, o2)));
        List<UserDto> dtos = UserDto.convertListToUserDto(Collections.singletonList(u), null);
        assertEquals(1, dtos.size());
        UserDto dto = dtos.get(0);
        assertEquals(100, dto.getUserId());
        assertEquals("Alice", dto.getName());
        assertEquals(2, dto.getOrders().size());
    }

    @Test
    void convertListToUserDtoWithFilterMapsOnlyMatchingOrder() {
        Date date = new Date();
        OrderEntity o1 = new OrderEntity(1, date, new HashSet<>());
        OrderEntity o2 = new OrderEntity(2, date, new HashSet<>());
        UserEntity u = new UserEntity(100, "Bob", new HashSet<>(Arrays.asList(o1, o2)));
        List<UserDto> dtos = UserDto.convertListToUserDto(Collections.singletonList(u), 1);
        assertEquals(1, dtos.size());
        UserDto dto = dtos.get(0);
        assertEquals(1, dto.getOrders().get(0).getOrderId());
        assertEquals(1, dto.getOrders().size());
    }

    @Test
    void convertListToUserDtoEmptyListReturnsEmpty() {
        List<UserDto> dtos = UserDto.convertListToUserDto(Collections.emptyList(), null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}
