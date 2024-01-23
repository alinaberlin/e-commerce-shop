package com.alinaberlin.ecommerceshop.payloads;

import jakarta.validation.constraints.NotNull;

public record AddProduct(@NotNull Long orderId, @NotNull Long productId) {
    public AddProduct {
    }
}
