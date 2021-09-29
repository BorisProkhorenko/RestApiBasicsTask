package com.epam.esm.mapper;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private static final String UTC_TIMEZONE = "UTC";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";


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

        return certificate;
    }

    private String toISOFormatDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone(UTC_TIMEZONE);
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        df.setTimeZone(tz);
        return df.format(date);
    }
}
