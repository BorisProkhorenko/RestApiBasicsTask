package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.filter.FilterType;
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
        Map<String, String> filters = new HashMap<>();
        tagId.ifPresent(s -> filters.put(FilterType.FILTER_BY_TAG_ID.getFilterName(), s));
        part.ifPresent(s -> filters.put(FilterType.FILTER_BY_PART.getFilterName(), s));
        nameSort.ifPresent(s -> filters.put(FilterType.SORT_BY_NAME.getFilterName(), s));
        descriptionSort.ifPresent(s -> filters.put(FilterType.SORT_BY_DESCRIPTION.getFilterName(), s));
        return getCertificatesWithParams(filters);
    }

    private List<GiftCertificate> getCertificatesWithParams(Map<String, String> filters) {
        List<GiftCertificate> certificates = getAllCertificates();
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            FilterType type = FilterType.findFilterType(entry.getKey());
            certificates = type.filter(certificates, entry.getValue());
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

    public GiftCertificate addTag(Long id, Long tagId) {
      return  dao.addTag(id, tagId);
    }

    public void removeTag(Long id, Long tagId) {
        dao.removeTag(id, tagId);
    }

}
