package com.epam.esm.dao;


import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class UserDaoImpl extends AbstractDao implements UserDao {

    private final OrderDao orderDao;

    public UserDaoImpl(SessionFactory sessionFactory, OrderDao orderDao) {
        super(sessionFactory);
        this.orderDao = orderDao;
    }

    @Override
    public User getById(Long id) {
        User user = getCurrentSession().get(User.class, id);
        if (user != null) {
            mapCertificates(user.getOrders());
            return user;
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User getUserByUsername(String username) {
        Query query = getCurrentSession().createQuery("from User where username =:username")
                .setParameter("username", username);
        User user = (User) query.uniqueResult();
        if (user == null) {
            throw new UserNotFoundException(username);

        }
        mapCertificates(user.getOrders());
        return user;
    }

    @Override
    public List<User> getAll(int start, int limit) {
        List<User> users = getCurrentSession().createQuery("from User")
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();
        users.stream()
                .map(User::getOrders)
                .forEach(this::mapCertificates);
        return users;
    }

    @Override
    public User create(User user) {
        try {
            getUserByUsername(user.getUsername());
        } catch (UserNotFoundException ex) {
            getCurrentSession().save(user);
        }
        return user;
    }

    @Override
    public long getCount() {
        return (long) getCurrentSession()
                .createQuery("select count(u) from User u")
                .uniqueResult();
    }

    @Override
    public void delete(User user) {
        getCurrentSession().delete(user);
    }


    private void mapCertificates(Set<Order> orderSet){
        orderSet.forEach(orderDao::mapSnapshots);
    }

}
