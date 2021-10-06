package com.epam.esm.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    private final RowMapper<Tag> tagRowMapper;

    private static final String ID = "gc_id";
    private static final String NAME = "gc_name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private static final String UTC_TIMEZONE = "UTC";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";

    public GiftCertificateMapper(RowMapper<Tag> tagRowMapper) {
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(resultSet.getLong(ID));
        certificate.setName(resultSet.getString(NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getDouble(PRICE));
        certificate.setDuration(resultSet.getInt(DURATION));
        Date createDate = resultSet.getTimestamp(CREATE_DATE);
        certificate.setCreateDate(toISOFormatDate(createDate));
        Date lastUpdateDate = resultSet.getTimestamp(LAST_UPDATE_DATE);
        certificate.setLastUpdateDate(toISOFormatDate(lastUpdateDate));

        return mapTags(certificate, resultSet, i);
    }

    private GiftCertificate mapTags(GiftCertificate certificate, ResultSet resultSet, int i) throws SQLException {
        Set<Tag> tags = new HashSet<>();
        if (resultSet.isLast()) {
            Tag tag = tagRowMapper.mapRow(resultSet, i);
            tags.add(tag);
            certificate.setTags(tags);
            return certificate;
        }

        while (resultSet.getLong(ID) == certificate.getId()) {
            Tag tag = tagRowMapper.mapRow(resultSet, i);
            tags.add(tag);
            if(resultSet.isLast()){
                break;
            }
            resultSet.next();
        }
        certificate.setTags(tags);

        if (resultSet.TYPE_SCROLL_INSENSITIVE == resultSet.getType()) {
            resultSet.previous();
        }
        return certificate;
    }

    private String toISOFormatDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone(UTC_TIMEZONE);
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        df.setTimeZone(tz);
        return df.format(date);
    }
}
