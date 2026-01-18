package com.alinaberlin.ecommerceshop.models.dtos;

import java.util.Objects;

public class OrderItemDTO {

    private OrderItemId id;

    private int quantity;

    private ProductDTO product;

    public OrderItemDTO(OrderItemId id, int quantity, ProductDTO product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public OrderItemDTO() {
    }

    public OrderItemId getId() {
        return id;
    }

    public void setId(OrderItemId id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDTO orderItem = (OrderItemDTO) o;
        return Objects.equals(getId(), orderItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
