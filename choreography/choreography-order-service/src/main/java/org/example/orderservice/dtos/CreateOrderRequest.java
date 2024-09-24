package org.example.orderservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.orderservice.enums.PaymentMethod;
import org.example.orderservice.enums.PaymentType;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderRequest {

    @JsonProperty("payment_type")
    private PaymentType paymentType;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("order_items")
    private List<OrderItemDto> items;
}
