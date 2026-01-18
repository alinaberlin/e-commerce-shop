package com.alinaberlin.ecommerceshop.models.dtos;


import java.math.BigDecimal;
import java.util.Objects;

public class ProductDTO {

    private Long id;

    private BigDecimal price;
    private String name;
    private String description;
    private int quantity;

    public ProductDTO() {
    }

    public ProductDTO(String name, String description, int quantity, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO product = (ProductDTO) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "{" + "id='" + id + '\'' + ", price=" + price + ", name='" + name + '\'' + ", description='" + description + '\'' + ", quantity=" + quantity + '}';
    }
}
