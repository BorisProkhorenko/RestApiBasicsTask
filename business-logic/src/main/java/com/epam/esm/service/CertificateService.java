package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;

import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    private final CertificateDao dao;

    public CertificateService(CertificateDao dao) {
        this.dao = dao;
    }

    public Certificate getCertificateById(Long id) {
        return dao.getCertificateById(id);
    }

    public List<Certificate> getAllCertificates() {
        return dao.getAllCertificates();
    }


    public List<Certificate> getCertificatesWithParams(Set<String> tagIdSet, Optional<String> part,
                                                       Optional<String> nameSort,
                                                       Optional<String> descriptionSort) {
        Map<String, String> operations = new HashMap<>();
        part.ifPresent(s -> operations.put(OperationType.FILTER_BY_PART.getOperationName(), s));
        nameSort.ifPresent(s -> operations.put(OperationType.SORT_BY_NAME.getOperationName(), s));
        descriptionSort.ifPresent(s -> operations.put(OperationType.SORT_BY_DESCRIPTION.getOperationName(), s));
        List<Certificate> certificates = getCertificatesFilteredByTags(tagIdSet);
        return getCertificatesWithParams(operations, certificates);
    }

    private List<Certificate> getCertificatesWithParams(Map<String, String> filters,
                                                        List<Certificate> certificates) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            OperationType type = OperationType.findOperationType(entry.getKey());
            certificates = type.process(certificates, entry.getValue());
        }

        return certificates;
    }

    private List<Certificate> getCertificatesFilteredByTags(Set<String> tagIdSet) {
        List<Certificate> certificates = getAllCertificates();
        for (String id : tagIdSet) {
           certificates = filterByTag(certificates, id);
        }
        return certificates;
    }

    public List<Certificate> filterByTag(List<Certificate> certificates, String value) {
        try {
            return certificates.stream()
                    .filter(certificate -> certificate.getTags().stream().anyMatch(tag ->
                            tag.getId() == Long.parseLong(value)))
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("id");
        }

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
