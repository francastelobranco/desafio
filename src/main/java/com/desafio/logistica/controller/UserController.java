package com.desafio.logistica.controller;

import com.desafio.logistica.dto.UserDto;
import com.desafio.logistica.openapi.controller.UserControllerOpenApi;
import com.desafio.logistica.service.ProcessFileService;
import com.desafio.logistica.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController implements UserControllerOpenApi {

    @Autowired
    ProcessFileService processFileService;

    @Autowired
    UserService userService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("file") MultipartFile file) {
        userService.saveUsers(processFileService.processFiles(file));
    }

    @GetMapping("/find")
    public ResponseEntity<List<UserDto>> findAllOrFilter(
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "startDate", required = false) LocalDate dateStart,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        return ResponseEntity.ok().body(userService.findUsersWithOrdersAndProducts(orderId, dateStart, endDate));
    }
}