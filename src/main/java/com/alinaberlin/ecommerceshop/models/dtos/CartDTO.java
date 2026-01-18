package com.alinaberlin.ecommerceshop.models.dtos;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CartDTO {

    private Long id;

    private Set<CartItemDTO> items = new HashSet<>();

    public CartDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<CartItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDTO cartDTO = (CartDTO) o;
        return Objects.equals(getId(), cartDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", items=" + items +
                '}';
    }
}
