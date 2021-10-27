package com.epam.esm.dao;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {

    private final SessionFactory sessionFactory;
    private final TagDao tagDao;

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
    public List<Certificate> getAllCertificates() {
        return getCurrentSession().createQuery("from Certificate ").list();

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
