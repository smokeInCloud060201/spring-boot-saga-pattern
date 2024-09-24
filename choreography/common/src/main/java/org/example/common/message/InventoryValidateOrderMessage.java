package org.example.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryValidateOrderMessage {
    private String orderId;
    private boolean success;
}
