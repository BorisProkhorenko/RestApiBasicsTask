package com.epam.esm.service;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.CertificateSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public CertificateSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public CertificateSpecificationsBuilder with(SearchCriteria.Operation operation, Object value) {
        params.add(new SearchCriteria(operation, value));
        return this;
    }

    public Specification<Certificate> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(CertificateSpecification::new)
                .collect(Collectors.toList());

        return concat(specs);

    }

    private Specification<Certificate> concat(List<Specification> specs) {
        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}

