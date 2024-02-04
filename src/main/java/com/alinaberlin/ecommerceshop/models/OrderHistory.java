package com.alinaberlin.ecommerceshop.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.util.Date;
import java.util.Objects;

@Entity
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Order order;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Date statusDate;

    public OrderHistory(Order order, OrderStatus orderStatus, Date statusDate) {
        this.order = order;
        this.orderStatus = orderStatus;
        this.statusDate = statusDate;
    }

    public OrderHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderHistory that = (OrderHistory) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", order=" + order +
                ", orderStatus=" + orderStatus +
                ", statusDate=" + statusDate +
                '}';
    }
}
