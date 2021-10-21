package com.epam.esm.dao;

import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
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
public class TagDaoImpl implements TagDao {


    private final SessionFactory sessionFactory;

    public TagDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag = getCurrentSession().get(Tag.class, id);
        if (tag != null) {
            return tag;
        } else {
            throw new TagNotFoundException(id);
        }

    }

    @Override
    public Tag getTagByName(String name) {
        Query query = getCurrentSession().createQuery("from Tag where name =:name")
                .setParameter("name", name);
        Tag tag = (Tag) query.uniqueResult();
        if (tag != null) {
            return tag;
        } else {
            throw new TagNotFoundException(name);
        }
    }


    @Override
    public List<Tag> getAllTags() {

        return getCurrentSession().createQuery("from Tag").list();
    }

    @Override
    public void deleteTag(Tag tag) {
        getCurrentSession().delete(tag);
    }

    @Override
    public Tag createTag(Tag tag) {
        try {
            getTagByName(tag.getName());
        } catch (TagNotFoundException ex) {
            getCurrentSession().saveOrUpdate(tag);
        }
        return tag;
    }

    public Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return sessionFactory.openSession();
        }
    }
}
