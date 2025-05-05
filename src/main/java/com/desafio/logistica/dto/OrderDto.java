package com.desafio.logistica.dto;

import com.desafio.logistica.model.OrderEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderDto {

    private Integer orderId;

    private BigDecimal total;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

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
