package com.epam.esm.dao;


import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class OrderDaoImpl extends AbstractDao implements OrderDao {

    private final CertificateDao certificateDao;
    private final CertificateJsonMapper jsonMapper;


    public OrderDaoImpl(SessionFactory sessionFactory, CertificateDao certificateDao, CertificateJsonMapper jsonMapper) {
        super(sessionFactory);
        this.certificateDao = certificateDao;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Order getById(Long id) {
        Order order = getCurrentSession().get(Order.class, id);
        if (order != null) {
            mapSnapshots(order);
            return order;
        } else {
            throw new OrderNotFoundException(id);
        }
    }

    @Override
    public List<Order> getAll(int start, int limit) {
        List<Order> orders = getCurrentSession().createQuery("from Order ")
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();
        return orders.stream()
                .peek(this::mapSnapshots)
                .collect(Collectors.toList());
    }

    @Override
    public Order create(Order order) {
        verifyCertificatesWithDb(order);
        createSnapshots(order);
        mapCost(order);
        getCurrentSession().save(order);
        return order;
    }


    @Override
    public void delete(Order order) {
        getCurrentSession().delete(order);
    }

    private void mapCost(Order order) {
        double cost = 0d;
        for (Certificate certificate : order.getCertificates()) {
            if (certificate.getPrice() != null) {
                cost += certificate.getPrice();
            }
        }
        order.setCost(cost);

    }

    private void verifyCertificatesWithDb(Order order) {
        if (order.getCertificates() != null) {
            List<Certificate> certificates = order.getCertificates();
            order.setCertificates(certificates);
            List<Certificate> certificatesFromDb = new ArrayList<>();
            for (Certificate cert : order.getCertificates()) {

                cert = certificateDao.getById(cert.getId());
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



}
