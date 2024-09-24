package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.BadOrderRequestException;
import org.example.common.exception.NotEnoughInventoryItemException;
import org.example.common.message.CheckoutInventoryMessage;
import org.example.common.message.InventoryMessage;
import org.example.common.message.InventoryValidateOrderMessage;
import org.example.common.util.JsonUtil;
import org.example.orderservice.dtos.CreateOrderRequest;
import org.example.orderservice.entity.Item;
import org.example.orderservice.entity.Order;
import org.example.orderservice.enums.OrderStatus;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;

    @Transactional(rollbackFor = {NotEnoughInventoryItemException.class, Exception.class})
    public void createNewOrder(CreateOrderRequest createOrderRequest) throws BadOrderRequestException {

        if (createOrderRequest.getPaymentType() == null || createOrderRequest.getPaymentMethod() == null || createOrderRequest.getItems().isEmpty()) {
            throw new BadOrderRequestException("The request is invalid");
        }

        BigDecimal itemsAmount = createOrderRequest.getItems().stream()
                .map(oi -> oi.getAmount().multiply(new BigDecimal(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxAmount = itemsAmount.multiply(BigDecimal.valueOf(0.1));
        BigDecimal totalAmount = itemsAmount.add(taxAmount);

        List<Item> orderItems = createOrderRequest.getItems().stream()
                .map(i -> {
                    Item item = new Item();
                    item.setInventoryId(i.getInventoryId());
                    item.setAmount(i.getAmount());
                    item.setInventoryName(i.getInventoryName());
                    return item;
                }).toList();

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId.toString());
        order.setAmount(totalAmount);
        order.setTaxAmount(taxAmount);
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());
        order.setPaymentType(createOrderRequest.getPaymentType());
        order.setItemList(orderItems);
        order.setDeleted(false);
        order.setOrderStatus(OrderStatus.CREATED);
        order = orderRepository.save(order);

        List<InventoryMessage> inventoryMessageList = createOrderRequest.getItems().stream()
                .map(i -> InventoryMessage.builder()
                        .inventoryId(i.getInventoryId().toString())
                        .quantity(i.getQuantity())
                        .build()).toList();

        CheckoutInventoryMessage checkoutInventoryMessage = CheckoutInventoryMessage.builder()
                .orderId(order.getOrderId())
                .inventoryMessageList(inventoryMessageList)
                .build();

        String message = JsonUtil.stringify(checkoutInventoryMessage);

        kafkaTemplate.send("checkout_inventory", message);
    }

    @KafkaListener(topics = "order_validate_inventory", groupId = "saga-group")
    public void handleNotEnoughInventory(String message) throws Exception {
        InventoryValidateOrderMessage inventoryValidateOrderMessage = JsonUtil.getObjectFromJsonString(InventoryValidateOrderMessage.class, message);
        if (inventoryValidateOrderMessage == null) {
            throw new BadOrderRequestException("Not found order Id from message");
        }

        Order order = orderRepository.findByOrderId(inventoryValidateOrderMessage.getOrderId()).orElse(null);
        if (order != null) {
            if (inventoryValidateOrderMessage.isSuccess()) {
                order.setOrderStatus(OrderStatus.CHECKOUT);
            } else {
                order.setOrderStatus(OrderStatus.NO_INVENTORY);
            }
            orderRepository.save(order);
        }
    }
}
