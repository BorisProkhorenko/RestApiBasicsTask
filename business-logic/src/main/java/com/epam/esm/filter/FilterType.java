package com.epam.esm.filter;

import com.epam.esm.model.GiftCertificate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum FilterType {
    FILTER_BY_TAG_ID("filter_by_tag_id") {
        @Override
        public List<GiftCertificate> filter(List<GiftCertificate> certificates, String value) {
            return certificates.stream()
                    .filter(certificate -> certificate.getTags().stream().anyMatch(tag ->
                            tag.getId() == Long.parseLong(value)))
                    .collect(Collectors.toList());
        }
    },

    FILTER_BY_PART("filter_by_part") {
        @Override
        public List<GiftCertificate> filter(List<GiftCertificate> certificates, String value) {
            return certificates.stream()
                    .filter(certificate -> certificate.getName().contains(value) ||
                            certificate.getDescription().contains(value))
                    .collect(Collectors.toList());
        }
    },

    SORT_BY_NAME("sort_by_name") {
        @Override
        public List<GiftCertificate> filter(List<GiftCertificate> certificates, String value) {
            return getGiftCertificates(certificates, value, Comparator.comparing(GiftCertificate::getName));
        }
    },

    SORT_BY_DESCRIPTION("sort_by_description") {
        @Override
        public List<GiftCertificate> filter(List<GiftCertificate> certificates, String value) {
            return getGiftCertificates(certificates, value, Comparator.comparing(GiftCertificate::getDescription));
        }
    };


    private String filterName;

    FilterType(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterName() {
        return filterName;
    }

    public abstract List<GiftCertificate> filter(List<GiftCertificate> certificates, String value);

    public static FilterType findFilterType(String filterName) {
        return Arrays.stream(FilterType.values())
                .filter(filterType -> filterType.getFilterName().equalsIgnoreCase(filterName))
                .findAny().orElseThrow(RuntimeException::new);
    }

    private static List<GiftCertificate> getGiftCertificates(List<GiftCertificate> certificates, String value,
                                                             Comparator<GiftCertificate> comparing) {
        if (value.equalsIgnoreCase("ASC")) {
            return certificates.stream().sorted(comparing)
                    .collect(Collectors.toList());

        } else if (value.equalsIgnoreCase("DESC")) {
            return certificates.stream().sorted(comparing.reversed())
                    .collect(Collectors.toList());
        }

        return certificates;
    }
}
