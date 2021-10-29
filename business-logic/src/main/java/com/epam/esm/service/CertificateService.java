package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    private final CertificateDao dao;
    private final static int CERTIFICATES_ON_PAGE = 5;

    public CertificateService(CertificateDao dao) {
        this.dao = dao;
    }

    public Certificate getCertificateById(Long id) {
        return dao.getCertificateById(id);
    }

    public List<Certificate> getAllCertificates(int page) {
        int start = calculateStartPage(page);
        return dao.getAllCertificates(start,CERTIFICATES_ON_PAGE);
    }
    public List<Certificate> getAllCertificates() {
        return dao.getAllCertificates();
    }

    public List<Certificate> getCertificatesWithParams(Set<Long> tagIdSet, Optional<String> part,
                                                       Optional<String> nameSort,
                                                       Optional<String> dateSort, int page) {

        int start = calculateStartPage(page);
        Set<Tag> tags = tagIdSet.stream()
                .map(Tag::new)
                .collect(Collectors.toSet());
        return dao.getCertificatesWithParams(tags,part,nameSort,dateSort,start,CERTIFICATES_ON_PAGE);
    }

    private int calculateStartPage(int page){
        page--;
        if(page<0){
            page=0;
        }
        return page * CERTIFICATES_ON_PAGE;
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
