package com.epam.esm.dao;

import com.epam.esm.dto.CertificateJsonMapper;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.OrderCertificate;
import com.epam.esm.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class OrderDaoImpl implements OrderDao {


    private final SessionFactory sessionFactory;
    private final CertificateDao certificateDao;
    private final CertificateJsonMapper jsonMapper;


    public OrderDaoImpl(SessionFactory sessionFactory, CertificateDao certificateDao, CertificateJsonMapper jsonMapper) {
        this.sessionFactory = sessionFactory;
        this.certificateDao = certificateDao;
        this.jsonMapper = jsonMapper;
    }


    @Override
    public Order getOrderById(Long id) {
        Order order = getCurrentSession().get(Order.class, id);
        if (order != null) {
            mapSnapshots(order);
            return order;
        } else {
            throw new OrderNotFoundException(id);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = getCurrentSession().createQuery("from Order ").list();
        return orders.stream()
                .peek(this::mapSnapshots)
                .collect(Collectors.toList());
    }

    @Override
    public Order createOrder(Order order) {
        verifyCertificatesWithDb(order);
        createSnapshots(order);
        getCurrentSession().save(order);
        return order;
    }


    private void verifyCertificatesWithDb(Order order) {
        if (order.getCertificates() != null) {
            List<Certificate> certificates = order.getCertificates();
            order.setCertificates(certificates);
            List<Certificate> certificatesFromDb = new ArrayList<>();
            for (Certificate cert : order.getCertificates()) {

                cert = certificateDao.getCertificateById(cert.getId());
                certificatesFromDb.add(cert);
            }
            order.setCertificates(certificatesFromDb);
        }

    }

    private void createSnapshots(Order order){
        List<OrderCertificate> orderCertificates = new ArrayList<>();
        for (Certificate certificate: order.getCertificates()) {
            String snapshot = jsonMapper.toJson(certificate);
            OrderCertificate orderCertificate = new OrderCertificate(order,certificate,snapshot);
            orderCertificates.add(orderCertificate);
        }
        order.setSnapshots(orderCertificates);
    }

    private void mapSnapshots(Order order){
        List<Certificate> certificates = new ArrayList<>();
        for (OrderCertificate orderCertificate: order.getSnapshots()) {
            String snapshot = orderCertificate.getSnapshot();
            Certificate certificate = jsonMapper.fromJson(snapshot);
            certificates.add(certificate);
        }
        order.setCertificates(certificates);
    }

    @Override
    public void deleteOrder(Order order) {
        getCurrentSession().delete(order);
    }

    public Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return sessionFactory.openSession();
        }
    }
}
