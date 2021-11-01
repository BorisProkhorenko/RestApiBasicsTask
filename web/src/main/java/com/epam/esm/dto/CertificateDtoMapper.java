package com.epam.esm.dto;

import com.epam.esm.model.Certificate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class CertificateDtoMapper extends DateMapper{

    private final TagDtoMapper tagDtoMapper;

    public CertificateDtoMapper(TagDtoMapper tagDtoMapper) {
        this.tagDtoMapper = tagDtoMapper;
    }

    public CertificateDto toDto(Certificate certificate) {
        String createDate = toISOFormatDate(certificate.getCreateDate());
        String updateDate = toISOFormatDate(certificate.getLastUpdateDate());
        Set<TagDto> tagDtoSet = certificate.getTags()
                .stream()
                .map(tagDtoMapper::toDto)
                .collect(Collectors.toSet());
        return new CertificateDto(certificate.getId(), certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), createDate, updateDate, tagDtoSet);
    }

}
