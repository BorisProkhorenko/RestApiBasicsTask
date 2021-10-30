package com.epam.esm.dao;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;


import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface CertificateDao {

    Certificate getCertificateById(Long id);

    List<Certificate> getAllCertificates(int start, int limit);

    List<Certificate> getAllCertificates();

    void deleteCertificate(Certificate certificate);

    Certificate updateCertificate(Certificate certificate);

    Certificate createCertificate(Certificate certificate);

    List<Certificate> getAllCertificates(Set<Tag> tagIdSet, Optional<String> part,
                                                Optional<String> nameSort, Optional<String> dateSort,
                                                int start, int limit);



}
