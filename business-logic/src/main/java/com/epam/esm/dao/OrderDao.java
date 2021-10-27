package com.epam.esm.dao;

import com.epam.esm.model.Order;

import java.util.List;

public interface OrderDao {

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order createOrder(Order order);

    void deleteOrder(Order order);
}
