package org.example.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutInventoryMessage {
    private String orderId;
    private List<InventoryMessage> inventoryMessageList;
}
