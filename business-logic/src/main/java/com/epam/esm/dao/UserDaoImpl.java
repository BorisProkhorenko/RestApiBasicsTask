package com.epam.esm.dao;

import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDaoImpl implements UserDao{


    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getUserById(Long id) {
        User user = getCurrentSession().get(User.class, id);
        if (user != null) {
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
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException(username);
        }
    }

    @Override
    public List<User> getAllUsers(int start, int limit) {
        return getCurrentSession().createQuery("from User")
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();
    }

    @Override
    public User createUser(User user) {
        try {
            getUserByUsername(user.getUsername());
        } catch (UserNotFoundException ex) {
            getCurrentSession().save(user);
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        getCurrentSession().delete(user);
    }

    public Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return sessionFactory.openSession();
        }
    }



}
