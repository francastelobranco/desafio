package com.desafio.logistica.dto;

import com.desafio.logistica.model.OrderEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "Pedido realizado por um usuário")
public class OrderDto {

    @Schema(description = "ID do pedido", example = "123")
    private Integer orderId;

    @Schema(description = "Valor total do pedido", example = "510.23")
    private BigDecimal total;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Data do pedido", example = "2025-01-02")
    private Date date;

    @Schema(description = "Lista de produtos do pedido")
    private List<ProductDto> products;

    public OrderDto(Integer orderId, BigDecimal total, Date date, List<ProductDto> products) {
        this.orderId = orderId;
        this.total = total;
        this.date = date;
        this.products = products;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public static List<OrderDto> convertListToOrderDto(Set<OrderEntity> orders) {
        return orders.stream().map(OrderDto::convertToOrderDto).collect(Collectors.toList());
    }

    private static OrderDto convertToOrderDto(OrderEntity order) {
        return new OrderDto(order.getOrderId(), order.getTotal(), order.getDate(), ProductDto.convertListToProductDto(order.getProducts()));
    }

}
