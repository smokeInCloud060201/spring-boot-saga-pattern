package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/created")
    public void createOrder() {
        orderService.createNewOrder();
    }
}
