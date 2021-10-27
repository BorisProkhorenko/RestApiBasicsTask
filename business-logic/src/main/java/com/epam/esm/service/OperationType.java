package com.epam.esm.service;

import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Certificate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum OperationType {


    FILTER_BY_PART("filter_by_part") {
        @Override
        public List<Certificate> process(List<Certificate> certificates, String value) {
            return certificates.stream()
                    .filter(certificate -> certificate.getName().contains(value) ||
                            certificate.getDescription().contains(value))
                    .collect(Collectors.toList());
        }
    },

    SORT_BY_NAME("sort_by_name") {
        @Override
        public List<Certificate> process(List<Certificate> certificates, String value) {
            return getGiftCertificates(certificates, value, Comparator.comparing(Certificate::getName));
        }
    },

    SORT_BY_DESCRIPTION("sort_by_description") {
        @Override
        public List<Certificate> process(List<Certificate> certificates, String value) {
            return getGiftCertificates(certificates, value, Comparator.comparing(Certificate::getDescription));
        }
    };


    private final String operationName;


    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    OperationType(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }

    public abstract List<Certificate> process(List<Certificate> certificates, String value);

    public static OperationType findOperationType(String filterName) {
        return Arrays.stream(OperationType.values())
                .filter(operationType -> operationType.getOperationName().equalsIgnoreCase(filterName))
                .findAny().orElseThrow(RuntimeException::new);
    }

    private static List<Certificate> getGiftCertificates(List<Certificate> certificates, String value,
                                                         Comparator<Certificate> comparing) {
        if (value.equalsIgnoreCase(ASC)) {
            return certificates.stream().sorted(comparing)
                    .collect(Collectors.toList());

        } else if (value.equalsIgnoreCase(DESC)) {
            return certificates.stream().sorted(comparing.reversed())
                    .collect(Collectors.toList());
        }

        throw new InvalidRequestException("sort parameters");
    }
}
