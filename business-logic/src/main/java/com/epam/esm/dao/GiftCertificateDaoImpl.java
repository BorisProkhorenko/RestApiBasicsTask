package com.epam.esm.dao;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Repository
@Transactional
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final SessionFactory sessionFactory;
    private final TagDao tagDao;

    public GiftCertificateDaoImpl(SessionFactory sessionFactory, TagDao tagDao) {
        this.sessionFactory = sessionFactory;
        this.tagDao = tagDao;
    }


    @Override
    public GiftCertificate getCertificateById(Long id) {
        GiftCertificate certificate = getCurrentSession().get(GiftCertificate.class, id);
        if (certificate != null) {
            return certificate;
        } else {
            throw new CertificateNotFoundException(id);
        }
    }

    @Override
    public List<GiftCertificate> getAllCertificates() {
        return getCurrentSession().createQuery("from GiftCertificate ").list();

    }

    @Override
    public void deleteCertificate(GiftCertificate certificate) {
        getCurrentSession().delete(certificate);
    }

    @Override
    public GiftCertificate updateCertificate(GiftCertificate certificate) {
        GiftCertificate certificateFromDb = getCertificateById(certificate.getId());
        certificate = mapFields(certificate, certificateFromDb);

        return (GiftCertificate) getCurrentSession().merge(certificate);
    }

    private GiftCertificate mapFields(GiftCertificate newCert, GiftCertificate certificateFromDb) {
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
    public GiftCertificate createCertificate(GiftCertificate certificate) {
        if(certificate.getTags()!=null){
            for (Tag t: certificate.getTags()) {
                try{
                    Tag tag = tagDao.getTagByName(t.getName());
                    t.setId(tag.getId());
                } catch (TagNotFoundException ignored){
                }finally {
                    getCurrentSession().clear();
                }
            }
        }
        getCurrentSession().save(certificate);
        return certificate;
    }

/*
    @Override
    public GiftCertificate updateCertificate(GiftCertificate certificate) {
        getCertificateById(certificate.getId());
        jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
                certificate.getId());
        removeTags(certificate);
        createTags(certificate);
        return certificate;
    }


    @Override
    public GiftCertificate createCertificate(GiftCertificate certificate) {
        jdbcTemplate.update(SQL_INSERT_CERTIFICATE,
                certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration());
        certificate.setId(getCreatedCertificateId());

        createTags(certificate);

        return certificate;
    }

    private long getCreatedCertificateId() {
        RowMapper<Long> rowMapper = (resultSet, i) -> resultSet.getLong(1);
        return jdbcTemplate.queryForObject(SQL_FIND_LAST_CERTIFICATE_ID, new Object[]{}, rowMapper);
    }

    private void createTags(GiftCertificate certificate) {
        if (certificate.getTags() != null) {

            for (Tag tag : certificate.getTags()) {
                tagDao.createTag(tag);
                addTag(certificate.getId(), tag.getName());
            }
        }
    }


    private void addTag(Long id, String tagName) {
        GiftCertificate certificate = getCertificateById(id);
        Tag tag = tagDao.getTagByName(tagName);
        boolean isTagAssociated = certificate.getTags().stream().anyMatch(t -> t.getId() == tag.getId());
        if (isTagAssociated) {
            return;
        }
        jdbcTemplate.update(SQL_INSERT_TAG_CERTIFICATE,
                tag.getId(), id);
    }


    private void removeTags(GiftCertificate certificate) {
        jdbcTemplate.update(SQL_DELETE_TAGS_CERTIFICATE,
                certificate.getId());
    }

 */

    public Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return sessionFactory.openSession();
        }
    }

}
