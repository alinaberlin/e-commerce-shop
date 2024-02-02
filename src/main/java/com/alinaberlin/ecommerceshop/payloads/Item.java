package com.alinaberlin.ecommerceshop.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record Item(@NotNull Long productId, @Min(1) int quantity) {
    public Item {
    }
}
