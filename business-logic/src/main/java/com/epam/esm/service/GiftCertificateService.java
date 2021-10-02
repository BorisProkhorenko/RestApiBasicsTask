package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateService {

    private final GiftCertificateDao dao;

    public GiftCertificateService(GiftCertificateDao dao) {
        this.dao = dao;
    }

    public GiftCertificate getCertificateById(Long id) {
        return dao.getCertificateById(id);
    }

    public List<GiftCertificate> getAllCertificates() {
        return dao.getAllCertificates();
    }

    public List<GiftCertificate> getAllCertificates(Optional<Tag> tag, Optional<String> part,
                                                    Optional<Boolean> nameDesc,
                                                    Optional<Boolean> descriptionDesc) {
        return dao.getAllCertificates(tag, part, nameDesc, descriptionDesc);
    }

    public void deleteCertificate(GiftCertificate certificate) {
        dao.deleteCertificate(certificate);
    }

    public GiftCertificate updateCertificate(GiftCertificate certificate) {
        return dao.updateCertificate(certificate);
    }

    public GiftCertificate createCertificate(GiftCertificate certificate) {
        return dao.createCertificate(certificate);
    }

    public void addTag(Tag tag, GiftCertificate certificate) {
        dao.addTag(tag, certificate);
    }

    public void removeTag(Tag tag, GiftCertificate certificate) {
        dao.removeTag(tag, certificate);
    }

}
