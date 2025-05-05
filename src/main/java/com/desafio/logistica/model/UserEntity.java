package com.desafio.logistica.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table (name = "tb_user")
public class UserEntity {

    @Id
    private Integer userId;

    @Column(length = 45, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<OrderEntity> orders = new HashSet<>();

    public UserEntity() {}

    public UserEntity(Integer userId, String name, Set<OrderEntity> orders) {
        this.userId = userId;
        this.name = name;
        this.orders = orders;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Set<OrderEntity> getOrders() {
        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
