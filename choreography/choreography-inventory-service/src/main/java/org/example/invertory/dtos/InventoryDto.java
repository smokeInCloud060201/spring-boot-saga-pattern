package org.example.invertory.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryDto {
    private Long id;
    private String name;
    private BigDecimal amount;
    private Long quantity;
}
