package com.epam.esm.dao;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



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


    public Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return sessionFactory.openSession();
        }
    }

}
