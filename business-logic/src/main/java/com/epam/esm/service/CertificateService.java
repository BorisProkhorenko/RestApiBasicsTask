package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.Certificate;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    private final CertificateDao dao;
    private final static int DEFAULT_LIMIT = 5;
    private final static int DEFAULT_OFFSET = 0;

    public CertificateService(CertificateDao dao) {
        this.dao = dao;
    }

    public Certificate getCertificateById(Long id) {
        return dao.getCertificateById(id);
    }

    public List<Certificate> getAllCertificates(){
        return dao.getAllCertificates();
    }


    public List<Certificate> getAllCertificates(Set<Long> tagIdSet, Optional<String> part,
                                                Optional<String> nameSort, Optional<String> dateSort,
                                                Optional<Integer> limit, Optional<Integer> offset) {

        int start = offset.orElse(DEFAULT_OFFSET);
        int lim = limit.orElse(DEFAULT_LIMIT);
        if (start < 0) {
            start = DEFAULT_OFFSET;
        }
        if (lim <= 0) {
            lim = DEFAULT_LIMIT;
        }
        Set<Tag> tags = tagIdSet.stream()
                .map(Tag::new)
                .collect(Collectors.toSet());
        return dao.getAllCertificates(tags, part, nameSort, dateSort, start, lim);
    }

    private int calculateStartPage(int page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        return page * DEFAULT_OFFSET;
    }

    public void deleteCertificate(Long id) {
        Certificate certificate = new Certificate();
        certificate.setId(id);
        dao.deleteCertificate(certificate);
    }

    public Certificate updateCertificate(Certificate certificate) {
        return dao.updateCertificate(certificate);
    }

    public Certificate createCertificate(Certificate certificate) {
        return dao.createCertificate(certificate);
    }


}
