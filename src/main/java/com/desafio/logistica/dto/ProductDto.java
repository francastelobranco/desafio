package com.desafio.logistica.dto;

import com.desafio.logistica.model.ProductEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDto {

    private Integer productId;

    private BigDecimal value;

    public ProductDto(Integer productId, BigDecimal value) {
        this.productId = productId;
        this.value = value;
    }

    public Integer getProductId() {
        return productId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public static List<ProductDto> convertListToProductDto(Set<ProductEntity> products) {
        return products.stream().map(ProductDto::convertToProductDto).collect(Collectors.toList());
    }

    private static ProductDto convertToProductDto(ProductEntity product) {
        return new ProductDto(product.getProductId(), product.getValue());
    }
}
