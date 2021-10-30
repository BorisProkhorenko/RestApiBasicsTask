package com.epam.esm.dto;

import com.epam.esm.model.Certificate;
import org.springframework.stereotype.Component;





@Component
public class CertificateDtoMapper extends DateMapper{

    public CertificateDto toDto(Certificate certificate) {
        String createDate = toISOFormatDate(certificate.getCreateDate());
        String updateDate = toISOFormatDate(certificate.getLastUpdateDate());

        return new CertificateDto(certificate.getId(), certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), createDate, updateDate, certificate.getTags());
    }

}
