package com.alinaberlin.ecommerceshop.models.dtos;

import java.util.Date;
import java.util.Objects;

public class OrderHistoryDTO {

    private Long id;
    private OrderDTO order;
    private OrderStatus orderStatus;
    private Date statusDate;

    public OrderHistoryDTO(OrderDTO order, OrderStatus orderStatus, Date statusDate) {
        this.order = order;
        this.orderStatus = orderStatus;
        this.statusDate = statusDate;
    }

    public OrderHistoryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
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
        OrderHistoryDTO that = (OrderHistoryDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderHistory{" +
                "id=" + id +
                ", order=" + order +
                ", orderStatus=" + orderStatus +
                ", statusDate=" + statusDate +
                '}';
    }
}
