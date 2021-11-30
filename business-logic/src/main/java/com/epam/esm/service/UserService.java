package com.epam.esm.service;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.OrderCertificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class UserService extends AbstractService<User> implements UserDetailsService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CertificateRepository certificateRepository;
    private final CertificateJsonMapper jsonMapper;


    public UserService(UserRepository userRepository, OrderRepository orderRepository, CertificateRepository certificateRepository, CertificateJsonMapper jsonMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.jsonMapper = jsonMapper;
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        mapCertificates(user.getOrders());
        return user;
    }

    public Order createOrder(Order order) {
        validateUser(order);
        validateCertWithDb(order);
        calculateCost(order);
        createSnapshots(order);
        return orderRepository.save(order);
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    public Order getOrderByUserAndId(Long userId, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Optional<User> optionalUser = userRepository.findById(userId);
        ifOrderPresent(optionalOrder, orderId);
        ifUserPresent(optionalUser, userId);
        User user = optionalUser.get();
        Order order = optionalOrder.get();
        return validateOrder(order, user);
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthority(user));
    }

    private void ifOrderPresent(Optional<Order> optionalOrder, Long orderId) {
        if (!optionalOrder.isPresent()) {
            throw new OrderNotFoundException(orderId);
        }
    }

    private void ifUserPresent(Optional<User> optionalUser, Long userId) {
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(userId);
        }
    }

    private void validateUser(Order order) {
        long userId = order.getUser().getId();
        Optional<User> user = userRepository.findById(userId);
        ifUserPresent(user, userId);
        order.setUser(user.get());
    }

    private Order validateOrder(Order order, User user) {
        if (order.getUser().equals(user)) {
            return order;
        } else throw new OrderNotFoundException(order.getId());
    }

    private void validateCertWithDb(Order order) {
        List<Certificate> certificates = new ArrayList<>();
        for (Certificate certificate : order.getCertificates()) {
            Optional<Certificate> optional = certificateRepository.findById(certificate.getId());
            if (optional.isPresent()) {
                certificates.add(optional.get());
            } else {
                throw new CertificateNotFoundException(certificate.getId());
            }
        }
        order.setCertificates(certificates);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return authorities;
    }

    private void mapCertificates(Set<Order> orderSet) {
        orderSet.forEach(this::mapSnapshots);
    }

    private void mapSnapshots(Order order) {
        List<Certificate> certificates = new ArrayList<>();
        for (OrderCertificate orderCertificate : order.getSnapshots()) {
            String snapshot = orderCertificate.getSnapshot();
            Certificate certificate = jsonMapper.fromJson(snapshot);
            certificates.add(certificate);
        }
        order.setCertificates(certificates);
    }

    private void createSnapshots(Order order) {
        List<OrderCertificate> orderCertificates = new ArrayList<>();
        for (Certificate certificate : order.getCertificates()) {
            String snapshot = jsonMapper.toJson(certificate);
            OrderCertificate orderCertificate = new OrderCertificate(order, certificate, snapshot);
            orderCertificates.add(orderCertificate);
        }
        order.setSnapshots(orderCertificates);
    }

    private void calculateCost(Order order) {
        double cost = 0d;
        for (Certificate certificate : order.getCertificates()) {
            if (certificate.getPrice() != null) {
                cost += certificate.getPrice();
            }
        }
        order.setCost(cost);

    }
}
