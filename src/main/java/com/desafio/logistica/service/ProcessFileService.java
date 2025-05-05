package com.desafio.logistica.service;

import com.desafio.logistica.exeption.ErrorFileException;
import com.desafio.logistica.model.OrderEntity;
import com.desafio.logistica.model.ProductEntity;
import com.desafio.logistica.model.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProcessFileService {

    public Set<UserEntity> processFiles(MultipartFile file) {
        Map<Integer, UserEntity> userMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addLineToUsers(line, userMap);
            }
        } catch (Exception e) {
            throw new ErrorFileException(file.getOriginalFilename());
        }

        return new HashSet<>(userMap.values());
    }

    private void addLineToUsers(String line, Map<Integer, UserEntity> userMap) throws ParseException {
        Integer userId = Integer.parseInt(line.substring(0, 10).trim());
        String name = line.substring(10, 55).trim();
        Integer orderId = Integer.parseInt(line.substring(55, 65).trim());
        Integer productId = Integer.parseInt(line.substring(65, 75).trim());
        BigDecimal value = new BigDecimal(line.substring(75, 87).trim());
        Date date = new SimpleDateFormat("yyyyMMdd").parse(line.substring(87, 95).trim());

        UserEntity user = userMap.getOrDefault(userId, new UserEntity(userId, name, new HashSet<>()));

        OrderEntity order = user.getOrders().stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseGet(() -> {
                    OrderEntity newOrder = new OrderEntity(orderId, date, new HashSet<>());
                    user.getOrders().add(newOrder);
                    return newOrder;
                });

        ProductEntity product = new ProductEntity(productId, value);
        order.getProducts().add(product);

        order.setTotal(order.getTotal().add(value));

        userMap.put(userId, user);
    }
}
