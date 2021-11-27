package com.epam.esm.service;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.model.Certificate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class CertificateService extends AbstractService<Certificate> {

    private final CertificateRepository repository;
    private final static String ASC = "asc";
    private final static String DESC = "desc";

    public CertificateService(CertificateRepository repository) {
        super(repository);
        this.repository = repository;
    }


    public Page<Certificate> findAll(Specification<Certificate> specification,
                                     Map<String, String> sorts,
                                     int page, int size) {

        Sort sort = buildSort(sorts);
        Pageable pageable = PageRequest.of(page, size, sort);
        if (specification == null) {
            return repository.findAll(pageable);
        }
        return repository.findAll(specification, pageable);

    }

    public Certificate update(Certificate certificate) {
        mapNonNullFields(certificate);
        return repository.save(certificate);
    }

    private Sort buildSort(Map<String, String> sorts) {
        Sort sort = Sort.sort(Certificate.class);

        for (Map.Entry<String, String> entry : sorts.entrySet()) {
            sort = addSort(entry, sort);
        }

        return sort;
    }

    private Sort addSort(Map.Entry<String, String> entry, Sort sort) {
        if (entry.getValue().equalsIgnoreCase(ASC)) {
            sort = sort.and(Sort.by(entry.getKey()).ascending());
        } else if (entry.getValue().equalsIgnoreCase(DESC)) {
            sort = sort.and(Sort.by(entry.getKey()).descending());
        }
        return sort;
    }


    private void mapNonNullFields(Certificate certificate) {
        Optional<Certificate> optional = repository.findById(certificate.getId());
        if (!optional.isPresent()) {
            throw new CertificateNotFoundException(certificate.getId());
        }
        Certificate fromDb = optional.get();
        checkName(certificate, fromDb);
        checkDescription(certificate, fromDb);
        checkPrice(certificate, fromDb);
        checkDuration(certificate, fromDb);
        checkDate(certificate, fromDb);
        checkTags(certificate, fromDb);
    }

    private void checkName(Certificate certificate, Certificate fromDb) {
        if (certificate.getName() == null) {
            certificate.setName(fromDb.getName());
        }
    }

    private void checkDescription(Certificate certificate, Certificate fromDb) {
        if (certificate.getDescription() == null) {
            certificate.setDescription(fromDb.getDescription());
        }
    }

    private void checkPrice(Certificate certificate, Certificate fromDb) {
        if (certificate.getPrice() == null) {
            certificate.setPrice(fromDb.getPrice());
        }
    }

    private void checkDate(Certificate certificate, Certificate fromDb) {
        if (certificate.getCreateDate() == null) {
            certificate.setCreateDate(fromDb.getCreateDate());
        }
    }

    private void checkDuration(Certificate certificate, Certificate fromDb) {
        if (certificate.getDuration() == null) {
            certificate.setDuration(fromDb.getDuration());
        }
    }

    private void checkTags(Certificate certificate, Certificate fromDb) {
        if (certificate.getTags() == null) {
            certificate.setTags(fromDb.getTags());
        }
    }
}
