package com.alinaberlin.ecommerceshop.models;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date date;
    private OrderType orderType;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name ="order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Collection<Product>  products;

    private OrderStatus orderStatus;

    public Order( Date date, OrderType orderType, Collection<Product> products, OrderStatus orderStatus, User user) {
        this.date = date;
        this.orderType = orderType;
        this.products = products;
        this.orderStatus = orderStatus;
        this.user = user;
    }

    public Order() {

    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getId() == order.getId() && Objects.equals(getDate(), order.getDate()) && getOrderType() == order.getOrderType() && Objects.equals(getUser(), order.getUser()) && Objects.equals(getProducts(), order.getProducts()) && getOrderStatus() == order.getOrderStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getOrderType(), getUser(), getProducts(), getOrderStatus());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", date=" + date +
                ", orderType=" + orderType +
                ", user=" + user +
                ", products=" + products +
                ", orderStatus=" + orderStatus +
                '}';
    }
}