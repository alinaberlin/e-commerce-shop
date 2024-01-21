package com.alinaberlin.ecommerceshop.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double price;
    @Column(unique = true)
    private String name;
    private String description;
    private int quantity;

    public Product() {
    }

    public Product(String name, String description, int quantity, double price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
        Product product = (Product) o;
        return getQuantity() == product.getQuantity() && Objects.equals(getId(), product.getId()) && Objects.equals(getPrice(), product.getPrice()) && Objects.equals(getName(), product.getName()) && Objects.equals(getDescription(), product.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrice(), getName(), getDescription(), getQuantity());
    }

    @Override
    public String toString() {
        return "{" + "id='" + id + '\'' + ", price=" + price + ", name='" + name + '\'' + ", description='" + description + '\'' + ", quantity=" + quantity + '}';
    }
}
