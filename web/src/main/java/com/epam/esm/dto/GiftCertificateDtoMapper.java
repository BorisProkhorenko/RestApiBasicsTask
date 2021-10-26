package com.epam.esm.dto;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Component;





@Component
public class GiftCertificateDtoMapper extends DateMapper{

    public GiftCertificateDto toDto(GiftCertificate certificate) {
        String createDate = toISOFormatDate(certificate.getCreateDate());
        String updateDate = toISOFormatDate(certificate.getLastUpdateDate());

        return new GiftCertificateDto(certificate.getId(), certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), createDate, updateDate, certificate.getTags());
    }

}
