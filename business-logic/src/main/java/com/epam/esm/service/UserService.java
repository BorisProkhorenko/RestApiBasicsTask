package com.epam.esm.service;

import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserService extends AbstractService<User> implements UserDetailsService {

    private final UserDao userDao;
    private final OrderDao orderDao;

    private final static int DEFAULT_LIMIT = 10;
    private final static int DEFAULT_OFFSET = 0;


    public UserService(UserDao userDao, OrderDao orderDao) {
        super(userDao);
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    public User getUserByUsername(String username){
        return userDao.getUserByUsername(username);
    }

    public Order createOrder(Order order) {
        User user = userDao.getById(order.getUser().getId());
        order.setUser(user);
        return orderDao.create(order);
    }

    public void deleteOrder(Order order) {
        orderDao.delete(order);
    }

    public Order getOrderByUserAndId(Long userId, Long orderId) {
        Order order = orderDao.getById(orderId);
        User user = userDao.getById(userId);
        if (order.getUser().equals(user)) {
            return order;
        } else throw new OrderNotFoundException(orderId);
    }


    @Override
    public int getDefaultOffset() {
        return DEFAULT_OFFSET;
    }

    @Override
    public int getDefaultLimit() {
        return DEFAULT_LIMIT;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return authorities;
    }
}
