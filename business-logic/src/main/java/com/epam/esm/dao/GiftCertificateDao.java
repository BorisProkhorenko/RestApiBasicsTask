package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {

    GiftCertificate getCertificateById(Long id);

    List<GiftCertificate> getAllCertificates();

    void deleteCertificate(GiftCertificate certificate);

    GiftCertificate updateCertificate(GiftCertificate certificate);

    GiftCertificate createCertificate(GiftCertificate certificate);
}
