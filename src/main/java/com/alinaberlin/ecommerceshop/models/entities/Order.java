package com.alinaberlin.ecommerceshop.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    private User user;

    @OneToMany
    @JoinColumn(name = "order_id")
    private Set<OrderItem> items = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private BigDecimal total;

    public Order(Date date, Set<OrderItem> items, OrderStatus orderStatus, User user) {
        this.date = date;
        this.orderStatus = orderStatus;
        this.user = user;
        this.items = items;
    }

    public Order(Date date, OrderStatus orderStatus, User user) {
        this.date = date;
        this.orderStatus = orderStatus;
        this.user = user;
    }

    public Order(Date date, OrderStatus orderStatus, User user, BigDecimal total) {
        this.date = date;
        this.orderStatus = orderStatus;
        this.user = user;
        this.total = total;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Order() {

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + user +
                ", items=" + items +
                ", orderStatus=" + orderStatus +
                ", total=" + total +
                '}';
    }
}
