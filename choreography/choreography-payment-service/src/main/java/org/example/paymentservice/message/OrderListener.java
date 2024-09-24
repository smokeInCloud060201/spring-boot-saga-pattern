package org.example.paymentservice.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderListener {

    @KafkaListener(topics = { "order-topic", "default-topic" }, groupId = "saga-group")
    public void getOrder(String message) {
      log.info("Get the message: {}", message);
    }
}
