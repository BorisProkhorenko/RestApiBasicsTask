package com.epam.esm.dto;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;



@Component
public class GiftCertificateDtoMapper {

    private static final String UTC_TIMEZONE = "UTC";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";

    public GiftCertificateDto toDto(GiftCertificate certificate) {
        String createDate = toISOFormatDate(certificate.getCreateDate());
        String updateDate = toISOFormatDate(certificate.getLastUpdateDate());

        return new GiftCertificateDto(certificate.getId(), certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), createDate, updateDate, certificate.getTags());
    }

    private String toISOFormatDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone(UTC_TIMEZONE);
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        df.setTimeZone(tz);
        return df.format(date);
    }

    public GiftCertificate toCertificate(GiftCertificateDto dto) {

        return new GiftCertificate(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice(),
                dto.getDuration(), dto.getTags());
    }


}
