package com.desafio.logistica.service;

import com.desafio.logistica.dto.OrderDto;
import com.desafio.logistica.dto.ProductDto;
import com.desafio.logistica.dto.UserDto;
import com.desafio.logistica.exeption.InvalidDateIntervalException;
import com.desafio.logistica.exeption.OrderNotFoundException;
import com.desafio.logistica.model.UserEntity;
import com.desafio.logistica.repository.UserRepository;
import com.desafio.logistica.utils.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private UserRepository repository;

    @Test
    void shouldSaveUsersAndInvokeSaveAll() {
        UserEntity u = new UserEntity(1, "Alice", new HashSet<>());
        u.getOrders().add(
                new com.desafio.logistica.model.OrderEntity(
                        10, Date.from(LocalDate.now().atStartOfDay(TimeZone.getDefault().toZoneId()).toInstant()),
                        new HashSet<>(Collections.singletonList(
                                new com.desafio.logistica.model.ProductEntity(100, new BigDecimal("15.00"))
                        ))
                )
        );
        Set<UserEntity> users = Collections.singleton(u);

        userService.saveUsers(users);

        ArgumentCaptor<Set<UserEntity>> captor = ArgumentCaptor.forClass(Set.class);
        verify(repository, times(1)).saveAll(captor.capture());
        assertSame(users, captor.getValue());
    }

    @Test
    void whenOrderIdDoesNotExistThenThrowsOrderNotFound() {
        when(orderService.existsById(123)).thenReturn(false);

        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);
        assertThrows(
                OrderNotFoundException.class,
                () -> userService.findUsersWithOrdersAndProducts(123, start, end)
        );
    }

    @Test
    void whenOrderIdIsNullThenThrowIllegalArgumentException() {
        Integer orderId = null;
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUsersWithOrdersAndProducts(orderId, start, end)
        );

        assertEquals("Order ID must not be null.", exception.getMessage());
    }


    @Test
    void shouldReturnUserDtosGroupedByOrderAndProduct() {
        when(orderService.existsById(100)).thenReturn(true);

        Date dt = DateFormatUtils.formatDate(LocalDate.of(2023, 5, 1));
        Object[] row1 = new Object[]{1, "Bob", 100, new BigDecimal("200.00"), dt, 200, new BigDecimal("150.00")};
        Object[] row2 = new Object[]{1, "Bob", 100, new BigDecimal("200.00"), dt, 201, new BigDecimal("50.00")};
        when(repository.findUserOrdersWithProducts(
                eq(100),
                any(Date.class),
                any(Date.class))
        ).thenReturn(Arrays.asList(row1, row2));

        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);
        List<UserDto> dtos = userService.findUsersWithOrdersAndProducts(100, start, end);

        assertEquals(1, dtos.size());
        UserDto u = dtos.get(0);
        assertEquals(1, u.getUserId());
        assertEquals("Bob", u.getName());
        assertEquals(1, u.getOrders().size());

        OrderDto o = u.getOrders().get(0);
        assertEquals(100, o.getOrderId());
        assertEquals(new BigDecimal("200.00"), o.getTotal());
        assertEquals(dt, o.getDate());

        List<ProductDto> products = o.getProducts();
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p ->
                p.getProductId() == 200 && p.getValue().equals(new BigDecimal("150.00"))
        ));
        assertTrue(products.stream().anyMatch(p ->
                p.getProductId() == 201 && p.getValue().equals(new BigDecimal("50.00"))
        ));
    }

    @Test
    void whenStartDateAfterEndDateThenThrowInvalidDateIntervalException() {
        when(orderService.existsById(100)).thenReturn(true);
        LocalDate startDate = LocalDate.of(2025, 12, 31);
        LocalDate endDate = LocalDate.of(2025, 1, 1);

        assertThrows(
                InvalidDateIntervalException.class,
                () -> userService.findUsersWithOrdersAndProducts(100, startDate, endDate)
        );
    }
}