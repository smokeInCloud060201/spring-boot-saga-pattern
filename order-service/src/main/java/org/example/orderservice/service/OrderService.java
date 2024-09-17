package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dtos.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    private final Random random = new Random();

    public void createNewOrder() {
        Order order = new Order();

        order.setId(String.valueOf(random.nextInt() * 1000000));
        order.setId(String.valueOf(new BigDecimal(19999000)));
        log.info("Order created with id {}", order.getId());

        kafkaTemplate.send("1", order).whenComplete((stringOrderSendResult, throwable) -> {
            if (throwable == null) {
                log.info("Send message successfully {}",stringOrderSendResult.getProducerRecord());
            } else {
                log.info("Send message successfully {}",stringOrderSendResult.getProducerRecord());
            }
        });

    }
}
