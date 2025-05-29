package com.desafio.logistica.dto;

import com.desafio.logistica.model.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "Produto pertencente a um pedido")
public class ProductDto {

    @Schema(description = "ID do produto", example = "10")
    private Integer productId;

    @Schema(description = "Valor do produto", example = "798.03")
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
