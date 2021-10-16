package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;

import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    public List<GiftCertificate> getCertificatesWithParams(Optional<String> tagId, Optional<String> part,
                                                           Optional<String> nameSort,
                                                           Optional<String> descriptionSort) {
        Map<String, String> operations = new HashMap<>();
        tagId.ifPresent(s -> operations.put(OperationType.FILTER_BY_TAG_ID.getOperationName(), s));
        part.ifPresent(s -> operations.put(OperationType.FILTER_BY_PART.getOperationName(), s));
        nameSort.ifPresent(s -> operations.put(OperationType.SORT_BY_NAME.getOperationName(), s));
        descriptionSort.ifPresent(s -> operations.put(OperationType.SORT_BY_DESCRIPTION.getOperationName(), s));
        return getCertificatesWithParams(operations);
    }

    private List<GiftCertificate> getCertificatesWithParams(Map<String, String> filters) {
        List<GiftCertificate> certificates = getAllCertificates();
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            OperationType type = OperationType.findOperationType(entry.getKey());
            certificates = type.process(certificates, entry.getValue());
        }

        return certificates;
    }

    public void deleteCertificate(Long id) {
        dao.deleteCertificateById(id);
    }

    public GiftCertificate updateCertificate(GiftCertificate certificate) {
        return dao.updateCertificate(certificate);
    }

    public GiftCertificate createCertificate(GiftCertificate certificate) {
        return dao.createCertificate(certificate);
    }



}
