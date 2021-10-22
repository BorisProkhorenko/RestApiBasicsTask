package com.epam.esm.dao;

import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class OrderDaoImpl implements OrderDao {


    private final SessionFactory sessionFactory;
    private final GiftCertificateDao certificateDao;

    public OrderDaoImpl(SessionFactory sessionFactory, GiftCertificateDao certificateDao) {
        this.sessionFactory = sessionFactory;
        this.certificateDao = certificateDao;
    }


    @Override
    public Order getOrderById(Long id) {
        Order order = getCurrentSession().get(Order.class, id);
        if (order != null) {
            return order;
        } else {
            throw new OrderNotFoundException(id);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return getCurrentSession().createQuery("from Order ").list();
    }

    @Override
    public Order createOrder(Order order) {
        verifyCertificatesWithDb(order);
        getCurrentSession().save(order);
        return order;
    }

    @Override
    public Order updateOrder(Order order) {
        verifyCertificatesWithDb(order);
        Order fromDb = getOrderById(order.getId());
        User user = fromDb.getUser();
        order.setUser(user);
        getCurrentSession().merge(order);
        return order;
    }

    private void verifyCertificatesWithDb(Order order) {
        if (order.getCertificates() != null) {
            Set<GiftCertificate> certificates = order.getCertificates();
            order.setCertificates(certificates);
            Set<GiftCertificate> certificatesFromDb = new HashSet<>();
            for (GiftCertificate cert : order.getCertificates()) {

                cert = certificateDao.getCertificateById(cert.getId());
                certificatesFromDb.add(cert);
            }
            order.setCertificates(certificatesFromDb);
        }

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
