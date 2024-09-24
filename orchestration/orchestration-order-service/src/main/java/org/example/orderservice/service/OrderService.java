package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dtos.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Random random = new Random();

    public void createNewOrder() {
        Order order = new Order();

        order.setId(String.valueOf(random.nextInt() * 1000000));
        log.info("Order created with id {}", order.getId());

        kafkaTemplate.send("order-topic", "Hello " + order.getId());

    }
}
