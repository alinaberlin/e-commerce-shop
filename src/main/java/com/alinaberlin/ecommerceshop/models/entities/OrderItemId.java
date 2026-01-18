package com.alinaberlin.ecommerceshop.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemId implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_id")
    private Long orderId;

    public OrderItemId(Long productId, Long orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }

    public OrderItemId() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemId that = (OrderItemId) o;
        return Objects.equals(getProductId(), that.getProductId()) && Objects.equals(getOrderId(), that.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getOrderId());
    }
}
