package com.epam.esm.service;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends AbstractService<User>{

    private final UserDao userDao;
    private final OrderDao orderDao;

    private final static int DEFAULT_LIMIT = 10;
    private final static int DEFAULT_OFFSET = 0;


    public UserService(UserDao userDao, OrderDao orderDao) {
        super(userDao);
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    public Order createOrder(Order order){
        User user = userDao.getById(order.getUser().getId());
        order.setUser(user);
        return orderDao.create(order);
    }

    public void deleteOrder(Order order){
        orderDao.delete(order);
    }

    public Order getOrderByUserAndId(Long userId, Long orderId){
        Order order = orderDao.getById(orderId);
        User user = userDao.getById(userId);
        if(order.getUser().equals(user)){
            return order;
        }
        else throw new OrderNotFoundException(orderId);
    }


    @Override
    public int getDefaultOffset() {
        return DEFAULT_OFFSET;
    }

    @Override
    public int getDefaultLimit() {
        return DEFAULT_LIMIT;
    }
}
