package com.desafio.logistica.dto;

import com.desafio.logistica.model.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ProductDtoTest {

    @Test
    void convertListToProductDtoMapsFieldsCorrectly() {
        ProductEntity p1 = new ProductEntity(1, new BigDecimal("5.00"));
        ProductEntity p2 = new ProductEntity(2, new BigDecimal("7.50"));
        Set<ProductEntity> products = new HashSet<>(Arrays.asList(p1, p2));

        List<ProductDto> dtos = ProductDto.convertListToProductDto(products);

        assertEquals(2, dtos.size());

        Set<Integer> ids = dtos.stream()
                .map(ProductDto::getProductId)
                .collect(Collectors.toSet());
        Set<BigDecimal> values = dtos.stream()
                .map(ProductDto::getValue)
                .collect(Collectors.toSet());

        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
        assertTrue(values.contains(new BigDecimal("5.00")));
        assertTrue(values.contains(new BigDecimal("7.50")));
    }

    @Test
    void convertListToProductDtoEmptySetReturnsEmptyList() {
        Set<ProductEntity> products = Collections.emptySet();
        List<ProductDto> dtos = ProductDto.convertListToProductDto(products);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}