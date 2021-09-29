package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {

    GiftCertificate getCertificateById(Long id);

    List<GiftCertificate> getAllCertificates();

    boolean deleteCertificate(GiftCertificate certificate);

    boolean updateCertificate(GiftCertificate certificate);

    boolean createCertificate(GiftCertificate certificate);
}
