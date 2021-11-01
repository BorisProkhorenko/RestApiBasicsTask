package com.epam.esm.dao;

import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.model.Identifiable;
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
public class UserDaoImpl extends AbstractDao implements UserDao{


    public UserDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public User getById(Long id) {
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
    public List<User> getAll(int start, int limit) {
        return getCurrentSession().createQuery("from User")
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();
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
    public void delete(User user) {
        getCurrentSession().delete(user);
    }

}
