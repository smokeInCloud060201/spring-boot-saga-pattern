package org.example.invertory.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.dtos.BaseResponse;
import org.example.invertory.dtos.InventoryDto;
import org.example.invertory.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    private BaseResponse<InventoryDto> insertNewInventory(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.insertNewInventory(inventoryDto);
    }
}
