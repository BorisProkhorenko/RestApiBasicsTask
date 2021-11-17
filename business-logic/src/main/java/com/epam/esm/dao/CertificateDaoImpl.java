package com.epam.esm.dao;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.*;
import java.util.*;


@Repository
@Transactional
public class CertificateDaoImpl extends AbstractDao implements CertificateDao {

    private final TagDao tagDao;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAGS = "tags";
    private static final String DATE = "lastUpdateDate";
    private static final String ASC = "asc";
    private static final String DESC = "desc";

    public CertificateDaoImpl(SessionFactory sessionFactory, TagDao tagDao) {
        super(sessionFactory);
        this.tagDao = tagDao;
    }


    @Override
    public Certificate getById(Long id) {
        Certificate certificate = getCurrentSession().get(Certificate.class, id);
        if (certificate == null) {
            throw new CertificateNotFoundException(id);

        }
        return certificate;
    }

    @Override
    public List<Certificate> getAll(int start, int limit) {
        return getAll(new HashSet<>(), Optional.empty(), Optional.empty(),
                Optional.empty(), start, limit);

    }


    @Override
    public void delete(Certificate certificate) {
        getCurrentSession().delete(certificate);
    }

    @Override
    public Certificate update(Certificate certificate) {
        Certificate certificateFromDb = getById(certificate.getId());
        certificate = mapFields(certificate, certificateFromDb);

        return (Certificate) getCurrentSession().merge(certificate);
    }

    private Certificate mapFields(Certificate newCert, Certificate certificateFromDb) {
        mapName(newCert,certificateFromDb);
        mapDescription(newCert,certificateFromDb);
        mapPrice(newCert,certificateFromDb);
        mapDuration(newCert,certificateFromDb);
        mapTags(newCert,certificateFromDb);
        return certificateFromDb;
    }

    private void mapName(Certificate newCert, Certificate certificateFromDb){
        if (newCert.getName() != null) {
            certificateFromDb.setName(newCert.getName());
        }
    }

    private void mapDescription(Certificate newCert, Certificate certificateFromDb){
        if (newCert.getDescription() != null) {
            certificateFromDb.setDescription(newCert.getDescription());
        }
    }

    private void mapPrice(Certificate newCert, Certificate certificateFromDb){
        if (newCert.getPrice() != null) {
            certificateFromDb.setPrice(newCert.getPrice());
        }
    }

    private void mapDuration(Certificate newCert, Certificate certificateFromDb){
        if (newCert.getDuration() != null) {
            certificateFromDb.setDuration(newCert.getDuration());
        }
    }

    private void mapTags(Certificate newCert, Certificate certificateFromDb){
        if (newCert.getTags() != null) {
            certificateFromDb.setTags(newCert.getTags());
        }
    }


    @Override
    public Certificate create(Certificate certificate) {
        if (certificate.getTags() != null) {
            for (Tag t : certificate.getTags()) {
                try {
                    Tag tag = tagDao.getTagByName(t.getName());
                    t.setId(tag.getId());
                } finally {
                    getCurrentSession().clear();
                }
            }
        }
        getCurrentSession().save(certificate);
        return certificate;
    }

    @Override
    public List<Certificate> getAll(Set<Tag> tagIdSet, Optional<String> part,
                                    Optional<String> nameSort, Optional<String> dateSort,
                                    int start, int limit) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Certificate> cq = cb.createQuery(Certificate.class);
        Root<Certificate> root = cq.from(Certificate.class);
        filter(cq, root, tagIdSet, part);
        List<Order> orderList = new ArrayList<>();
        nameSort.ifPresent(s -> sort(orderList, root, s, NAME));
        dateSort.ifPresent(s -> sort(orderList, root, s, DATE));
        cq.orderBy(orderList);
        return getCurrentSession().createQuery(cq)
                .setFirstResult(start)
                .setMaxResults(limit)
                .list();

    }

    @Override
    public long getCount() {
        return (long) getCurrentSession()
                .createQuery("select count(c) from Certificate c")
                .uniqueResult();
    }

    private void filter(CriteriaQuery<Certificate> cq, Root<Certificate> root, Set<Tag> tagIdSet, Optional<String> part) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
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

    private void sort(List<Order> orderList, Root<Certificate> root, String sortOrder, String rootField) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        if (sortOrder.equalsIgnoreCase(ASC)) {
            orderList.add((cb.asc(root.get(rootField))));
        } else if (sortOrder.equalsIgnoreCase(DESC)) {
            orderList.add((cb.desc(root.get(rootField))));
        } else {
            throw new InvalidRequestException("Sort can be only asc/desc");
        }
    }

}
