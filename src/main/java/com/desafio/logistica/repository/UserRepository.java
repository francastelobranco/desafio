package com.desafio.logistica.repository;

import com.desafio.logistica.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {

    @Query("""
    select u.userId, u.name, o.orderId, o.total, o.date, p.productId, p.value
    from UserEntity u
    join u.orders o
    join o.products p
    where (:orderId is null or o.orderId = :orderId)
      and (:startDate is null or o.date >= :startDate)
      and (:endDate is null or o.date <= :endDate)
""")
    List<Object[]> findUserOrdersWithProducts(
            @Param("orderId") Integer orderId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
