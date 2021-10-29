package com.epam.esm.dao;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {

    private final SessionFactory sessionFactory;
    private final TagDao tagDao;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAGS = "tags";
    private static final String DATE = "lastUpdateDate";
    private static final String ASC = "asc";
    private static final String DESC = "desc";

    public CertificateDaoImpl(SessionFactory sessionFactory, TagDao tagDao) {
        this.sessionFactory = sessionFactory;
        this.tagDao = tagDao;
    }


    @Override
    public Certificate getCertificateById(Long id) {
        Certificate certificate = getCurrentSession().get(Certificate.class, id);
        if (certificate != null) {
            return certificate;
        } else {
            throw new CertificateNotFoundException(id);
        }
    }

    @Override
    public List<Certificate> getAllCertificates(int start, int limit) {
        return getCurrentSession().createQuery("from Certificate ")
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();

    }

    @Override
    public List<Certificate> getAllCertificates() {
        return getCurrentSession().createQuery("from Certificate ")
                .list();

    }

    @Override
    public void deleteCertificate(Certificate certificate) {
        getCurrentSession().delete(certificate);
    }

    @Override
    public Certificate updateCertificate(Certificate certificate) {
        Certificate certificateFromDb = getCertificateById(certificate.getId());
        certificate = mapFields(certificate, certificateFromDb);

        return (Certificate) getCurrentSession().merge(certificate);
    }

    private Certificate mapFields(Certificate newCert, Certificate certificateFromDb) {
        if (newCert.getName() != null) {
            certificateFromDb.setName(newCert.getName());
        }
        if (newCert.getDescription() != null) {
            certificateFromDb.setDescription(newCert.getDescription());
        }
        if (newCert.getPrice() != null) {
            certificateFromDb.setPrice(newCert.getPrice());
        }
        if (newCert.getDuration() != null) {
            certificateFromDb.setDuration(newCert.getDuration());
        }
        if (newCert.getTags() != null) {
            certificateFromDb.setTags(newCert.getTags());
        }
        return certificateFromDb;
    }


    @Override
    public Certificate createCertificate(Certificate certificate) {
        if (certificate.getTags() != null) {
            for (Tag t : certificate.getTags()) {
                try {
                    Tag tag = tagDao.getTagByName(t.getName());
                    t.setId(tag.getId());
                } catch (TagNotFoundException ignored) {
                } finally {
                    getCurrentSession().clear();
                }
            }
        }
        getCurrentSession().save(certificate);
        return certificate;
    }


    public Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return sessionFactory.openSession();
        }
    }

    @Override
    public List<Certificate> getCertificatesWithParams(Set<Tag> tagIdSet, Optional<String> part,
                                                       Optional<String> nameSort, Optional<String> dateSort,
                                                       int start, int limit) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Certificate> cq = cb.createQuery(Certificate.class);
        filter(cq, tagIdSet, part);

        List<Order> orderList = new ArrayList<>();

        nameSort.ifPresent(s -> sort(orderList,cq, s, NAME));
        dateSort.ifPresent(s -> sort(orderList,cq, s, DATE));

        cq.orderBy(orderList);

        return getCurrentSession().createQuery(cq)
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();

    }

    private void filter(CriteriaQuery<Certificate> cq, Set<Tag> tagIdSet, Optional<String> part) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        Root<Certificate> root = cq.from(Certificate.class);
        Expression<Set<Tag>> tags = root.get(TAGS);
        Predicate predicate = cb.conjunction();
        if (!tagIdSet.isEmpty()) {
            for (Tag t : tagIdSet) {
                predicate = cb.and(predicate, cb.isMember(t, tags));
            }
        }
        if (part.isPresent()) {
            Predicate namePredicate = cb.like(root.get(NAME), "%" + part.get() + "%");
            Predicate descriptionPredicate = cb.like(root.get(DESCRIPTION), "%" + part.get() + "%");
            predicate = cb.and(predicate, namePredicate, descriptionPredicate);
        }

        cq.select(root).where(predicate);


    }

    private void sort(List<Order> orderList,CriteriaQuery<Certificate> cq, String sortOrder, String rootField) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        Root<Certificate> root = cq.from(Certificate.class);
        if (sortOrder.equalsIgnoreCase(ASC)) {
            orderList.add((cb.asc(root.get(rootField))));
        } else if (sortOrder.equalsIgnoreCase(DESC)) {
            orderList.add((cb.desc(root.get(rootField))));
        } else {
            throw new InvalidRequestException("Sort can be only asc/desc");
        }
    }

}
