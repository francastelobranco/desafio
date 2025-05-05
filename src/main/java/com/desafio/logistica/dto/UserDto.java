package com.desafio.logistica.dto;

import com.desafio.logistica.model.OrderEntity;
import com.desafio.logistica.model.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDto {

    private Integer userId;

    private String name;

    private List<OrderDto> orders;

    public UserDto() {
    }

    public UserDto(Integer userId, String name, List<OrderDto> orders) {
        this.userId = userId;
        this.name = name;
        this.orders = orders;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public static List<UserDto> convertListToUserDto(List<UserEntity> users, Integer orderIdFilter) {
        return users.stream().map(
                userEntity -> {
                    UserDto userDto = new UserDto();
                    userDto.setName(userEntity.getName());
                    userDto.setUserId(userEntity.getUserId());

                    Set<OrderEntity> filteredOrders = userEntity.getOrders().stream().filter(
                            order -> orderIdFilter == null || order.getOrderId().equals(orderIdFilter)
                    ).collect(Collectors.toSet());

                    userDto.setOrders(OrderDto.convertListToOrderDto(filteredOrders));
                    return userDto;
                }
        ).collect(Collectors.toList());
    }
}
