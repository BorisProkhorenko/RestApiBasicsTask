package com.epam.esm.service;

import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum OperationType {

    FILTER_BY_TAG_ID("filter_by_tag_id") {
        @Override
        public List<GiftCertificate> process(List<GiftCertificate> certificates, String value) {
            try {
                return certificates.stream()
                        .filter(certificate -> certificate.getTags().stream().anyMatch(tag ->
                                tag.getId() == Long.parseLong(value)))
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                LOGGER.error(e.getMessage(), e);
                throw new InvalidRequestException("id");
            }

        }
    },

    FILTER_BY_PART("filter_by_part") {
        @Override
        public List<GiftCertificate> process(List<GiftCertificate> certificates, String value) {
            return certificates.stream()
                    .filter(certificate -> certificate.getName().contains(value) ||
                            certificate.getDescription().contains(value))
                    .collect(Collectors.toList());
        }
    },

    SORT_BY_NAME("sort_by_name") {
        @Override
        public List<GiftCertificate> process(List<GiftCertificate> certificates, String value) {
            return getGiftCertificates(certificates, value, Comparator.comparing(GiftCertificate::getName));
        }
    },

    SORT_BY_DESCRIPTION("sort_by_description") {
        @Override
        public List<GiftCertificate> process(List<GiftCertificate> certificates, String value) {
            return getGiftCertificates(certificates, value, Comparator.comparing(GiftCertificate::getDescription));
        }
    };


    private final String operationName;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    OperationType(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }

    public abstract List<GiftCertificate> process(List<GiftCertificate> certificates, String value);

    public static OperationType findOperationType(String filterName) {
        return Arrays.stream(OperationType.values())
                .filter(operationType -> operationType.getOperationName().equalsIgnoreCase(filterName))
                .findAny().orElseThrow(RuntimeException::new);
    }

    private static List<GiftCertificate> getGiftCertificates(List<GiftCertificate> certificates, String value,
                                                             Comparator<GiftCertificate> comparing) {
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
