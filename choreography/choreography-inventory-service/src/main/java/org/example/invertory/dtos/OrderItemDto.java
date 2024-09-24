package org.example.invertory.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemDto {

    @JsonProperty("id")
    private Long inventoryId;

    @JsonProperty("name")
    private String inventoryName;

    @JsonProperty("amount")
    private BigDecimal amount;
}
