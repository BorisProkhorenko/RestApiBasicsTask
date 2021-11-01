package com.epam.esm.dao;

import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TagDaoImpl extends AbstractDao implements TagDao {

    private static final String HQL_GET_TAG_BY_ORDERS =" select tag FROM User u join u.orders o" +
            " join o.certificates c join c.tags tag group by tag.id order by" +
            " sum(o.cost) desc";

    public TagDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Tag getById(Long id) {
        Tag tag = getCurrentSession().get(Tag.class, id);
        if (tag != null) {
            return tag;
        } else {
            throw new TagNotFoundException(id);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
    public List<Tag> getAll(int start, int limit) {
        return getCurrentSession().createQuery("from Tag")
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();
    }

    @Override
    public void delete(Tag tag) {
        getCurrentSession().delete(tag);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Tag create(Tag tag) {
        try {
            getTagByName(tag.getName());
        } catch (TagNotFoundException ex) {
            getCurrentSession().save(tag);
        }
        return tag;
    }

    @Override
    public Tag getMostUsedTagOfUserWithHighestOrdersCost(){
        return  (Tag) getCurrentSession().createQuery(HQL_GET_TAG_BY_ORDERS)
                .setMaxResults(1)
                .uniqueResult();
    }

}
