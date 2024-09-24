package org.example.invertory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.dtos.BaseResponse;
import org.example.common.message.CheckoutInventoryMessage;
import org.example.common.message.InventoryMessage;
import org.example.common.message.InventoryValidateOrderMessage;
import org.example.common.util.JsonUtil;
import org.example.invertory.dtos.InventoryDto;
import org.example.invertory.entity.Inventory;
import org.example.invertory.entity.InventoryQuantity;
import org.example.invertory.repository.InventoryQuantityRepository;
import org.example.invertory.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryQuantityRepository inventoryQuantityRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = {"checkout_inventory", "default_topic"}, groupId = "saga-group")
    @Transactional
    public void checkAvailableInventory(String message) {
        CheckoutInventoryMessage orderMessage = JsonUtil.getObjectFromJsonString(CheckoutInventoryMessage.class, message);
        log.info("Get message in inventory service with topic checkout_inventory");
        InventoryValidateOrderMessage.InventoryValidateOrderMessageBuilder orderNotEnoughInventory = InventoryValidateOrderMessage.builder();
        Map<Long, Long> inventoryQuantityMap = orderMessage.getInventoryMessageList().stream()
                .collect(Collectors.toMap(inventoryMessage -> Long.parseLong(inventoryMessage.getInventoryId()), InventoryMessage::getQuantity));

        List<Inventory> inventoryList = inventoryRepository.findInventoriesByIds(inventoryQuantityMap.keySet());

        boolean isNotEnoughInventory = inventoryList.stream().anyMatch(s -> {
            Long itemAmount = inventoryQuantityMap.get(s.getId());
            return itemAmount == null || itemAmount > s.getInventoryQuantity().getQuantity();
        });

        orderNotEnoughInventory.orderId(orderMessage.getOrderId());
        if (!isNotEnoughInventory) {
            orderNotEnoughInventory.success(true);
        }
        kafkaTemplate.send("order_validate_inventory", JsonUtil.stringify(orderNotEnoughInventory.build()));
    }

    public BaseResponse<InventoryDto> insertNewInventory(InventoryDto inventoryDto) {

        Inventory inventory = new Inventory();
        inventory.setAmount(inventoryDto.getAmount());
        inventory.setName(inventoryDto.getName());
        inventory.setDeleted(false);
        inventory = inventoryRepository.save(inventory);

        InventoryQuantity inventoryQuantity = new InventoryQuantity();
        inventoryQuantity.setQuantity(inventoryDto.getQuantity());
        inventoryQuantity.setInventory(inventory);
        inventoryQuantityRepository.save(inventoryQuantity);


        return BaseResponse.<InventoryDto>builder()
                .data(InventoryDto.builder()
                        .id(inventory.getId())
                        .name(inventory.getName())
                        .quantity(inventoryQuantity.getQuantity())
                        .amount(inventory.getAmount())
                        .build())
                .build();
    }

}
