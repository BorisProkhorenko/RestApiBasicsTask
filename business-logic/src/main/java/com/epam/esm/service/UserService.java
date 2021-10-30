package com.epam.esm.service;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;
    private final OrderDao orderDao;

    private final static int DEFAULT_LIMIT = 10;
    private final static int DEFAULT_OFFSET = 0;


    public UserService(UserDao userDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
    }


    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public List<User> getAllUsers(Optional<Integer> limit, Optional<Integer> offset) {

        int start = offset.orElse(DEFAULT_OFFSET);
        int lim = limit.orElse(DEFAULT_LIMIT);
        if (start < 0) {
            start = DEFAULT_OFFSET;
        }
        if (lim <= 0) {
            lim = DEFAULT_LIMIT;
        }
        return userDao.getAllUsers(start, lim);
    }


    public Order createOrder(Order order){
        User user = userDao.getUserById(order.getUser().getId());
        order.setUser(user);
        return orderDao.createOrder(order);
    }

    public void deleteOrder(Long id){
        Order order = new Order();
        order.setId(id);
        orderDao.deleteOrder(order);
    }

    public Order getOrderByUserAndId(Long userId, Long orderId){
        Order order = orderDao.getOrderById(orderId);
        User user = userDao.getUserById(userId);
        if(order.getUser().equals(user)){
            return order;
        }
        else throw new OrderNotFoundException(orderId);
    }


}
