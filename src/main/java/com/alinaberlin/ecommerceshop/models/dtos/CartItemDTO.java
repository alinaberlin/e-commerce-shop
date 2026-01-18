package com.alinaberlin.ecommerceshop.models.dtos;

public class CartItemDTO {

    private CartItemId id;
    private int quantity;
    private ProductDTO product;

    public CartItemDTO(CartItemId id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public CartItemDTO(CartItemId id, int quantity, ProductDTO product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public CartItemDTO() {
    }

    public CartItemId getId() {
        return id;
    }

    public void setId(CartItemId id) {
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
}
