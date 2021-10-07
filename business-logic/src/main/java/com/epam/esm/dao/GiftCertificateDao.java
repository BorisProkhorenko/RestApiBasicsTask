package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;


public interface GiftCertificateDao {

    GiftCertificate getCertificateById(Long id);

    List<GiftCertificate> getAllCertificates();

    void deleteCertificateById(Long id);

    GiftCertificate updateCertificate(GiftCertificate certificate);

    GiftCertificate createCertificate(GiftCertificate certificate);

    void addTag(Long id, Long tagId);

    void removeTag(Long id, Long tagId);


}