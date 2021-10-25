package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.GiftCertificate;

import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

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


    public List<GiftCertificate> getCertificatesWithParams(Set<String> tagIdSet, Optional<String> part,
                                                           Optional<String> nameSort,
                                                           Optional<String> descriptionSort) {
        Map<String, String> operations = new HashMap<>();
        part.ifPresent(s -> operations.put(OperationType.FILTER_BY_PART.getOperationName(), s));
        nameSort.ifPresent(s -> operations.put(OperationType.SORT_BY_NAME.getOperationName(), s));
        descriptionSort.ifPresent(s -> operations.put(OperationType.SORT_BY_DESCRIPTION.getOperationName(), s));
        List<GiftCertificate> certificates = getCertificatesFilteredByTags(tagIdSet);
        return getCertificatesWithParams(operations, certificates);
    }

    private List<GiftCertificate> getCertificatesWithParams(Map<String, String> filters,
                                                            List<GiftCertificate> certificates) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            OperationType type = OperationType.findOperationType(entry.getKey());
            certificates = type.process(certificates, entry.getValue());
        }

        return certificates;
    }

    private List<GiftCertificate> getCertificatesFilteredByTags(Set<String> tagIdSet) {
        List<GiftCertificate> certificates = getAllCertificates();
        for (String id : tagIdSet) {
           certificates = filterByTag(certificates, id);
        }
        return certificates;
    }

    public List<GiftCertificate> filterByTag(List<GiftCertificate> certificates, String value) {
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
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(id);
        dao.deleteCertificate(certificate);
    }

    public GiftCertificate updateCertificate(GiftCertificate certificate) {
        return dao.updateCertificate(certificate);
    }

    public GiftCertificate createCertificate(GiftCertificate certificate) {
        return dao.createCertificate(certificate);
    }


}
