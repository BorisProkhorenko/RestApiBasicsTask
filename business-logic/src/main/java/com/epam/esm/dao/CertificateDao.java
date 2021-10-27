package com.epam.esm.dao;

import com.epam.esm.model.Certificate;


import java.util.List;


public interface CertificateDao {

    Certificate getCertificateById(Long id);

    List<Certificate> getAllCertificates();

    void deleteCertificate(Certificate certificate);

    Certificate updateCertificate(Certificate certificate);

    Certificate createCertificate(Certificate certificate);




}
