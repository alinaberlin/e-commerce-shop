package com.alinaberlin.ecommerceshop.payloads;

import com.alinaberlin.ecommerceshop.models.entities.OrderStatus;

public record UpdateOrderStatus(OrderStatus status) {
    public UpdateOrderStatus {
    }
}
