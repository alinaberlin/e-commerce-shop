package com.alinaberlin.ecommerceshop.models.dtos;

import java.io.Serializable;
import java.util.Objects;

public class CartItemId implements Serializable {

    private Long productId;

    private Long cartId;

    public CartItemId(Long productId, Long cartId) {
        this.productId = productId;
        this.cartId = cartId;
    }

    public CartItemId() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemId that = (CartItemId) o;
        return Objects.equals(getProductId(), that.getProductId()) && Objects.equals(getCartId(), that.getCartId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getCartId());
    }

    @Override
    public String toString() {
        return "{" +
                "productId=" + productId +
                ", cartId=" + cartId +
                '}';
    }
}
