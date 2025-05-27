package com.desafio.logistica.service;


import com.desafio.logistica.dto.OrderDto;
import com.desafio.logistica.dto.ProductDto;
import com.desafio.logistica.dto.UserDto;
import com.desafio.logistica.exeption.OrderNotFoundException;
import com.desafio.logistica.model.OrderEntity;
import com.desafio.logistica.model.ProductEntity;
import com.desafio.logistica.model.UserEntity;
import com.desafio.logistica.repository.UserRepository;
import com.desafio.logistica.utils.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.desafio.logistica.utils.ConstantsUtils.ERROR_ORDER_ID_NULL;

@Service
public class UserService {

    @Autowired
    OrderService orderService;

    @Autowired
    UserRepository repository;

    public void saveUsers(Set<UserEntity> users) {
        Map<Integer, ProductEntity> produtoUnico = new HashMap<>();

        for (UserEntity user : users) {
            for (OrderEntity order : user.getOrders()) {
                Set<ProductEntity> novosProdutos = new HashSet<>();

                for (ProductEntity produto : order.getProducts()) {
                    if (produtoUnico.containsKey(produto.getProductId())) {
                        novosProdutos.add(produtoUnico.get(produto.getProductId()));
                    } else {
                        produtoUnico.put(produto.getProductId(), produto);
                        novosProdutos.add(produto);
                    }
                }
                order.setProducts(novosProdutos);
            }
        }


        repository.saveAll(users);
    }

    public List<UserDto> findUsersWithOrdersAndProducts(
            Integer orderId, LocalDate startDate, LocalDate endDate) {

        if (orderId != null) {
            validateExistingOrder(orderId);
        }

        Date start = DateFormatUtils.formatDate(startDate);
        Date end = DateFormatUtils.formatDate(endDate);

        List<Object[]> results = repository.findUserOrdersWithProducts(orderId, start, end);

        Map<Integer, UserDto> userMap = new HashMap<>();

        for (Object[] row : results) {
            Integer userId = (Integer) row[0];
            String userName = (String) row[1];
            Integer ordId = (Integer) row[2];
            BigDecimal ordTotal = (BigDecimal) row[3];
            Date ordDate = (Date) row[4];
            Integer prodId = (Integer) row[5];
            BigDecimal prodValue = (BigDecimal) row[6];

            userMap.putIfAbsent(userId, new UserDto(userId, userName, new ArrayList<>()));

            UserDto userDto = userMap.get(userId);

            OrderDto orderDto = userDto.getOrders()
                    .stream()
                    .filter(o -> o.getOrderId().equals(ordId))
                    .findFirst()
                    .orElseGet(() -> {
                        OrderDto o = new OrderDto(ordId, ordTotal, ordDate, new ArrayList<>());
                        userDto.getOrders().add(o);
                        return o;
                    });

            orderDto.getProducts().add(new ProductDto(prodId, prodValue));
        }

        return new ArrayList<>(userMap.values());
    }

    private void validateExistingOrder(Integer orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException(ERROR_ORDER_ID_NULL);
        }

        if (!orderService.existsById(orderId)) {
           throw new OrderNotFoundException(orderId);
       }
    }
}