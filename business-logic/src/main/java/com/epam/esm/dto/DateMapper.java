package com.epam.esm.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateMapper {

    private static final String UTC_TIMEZONE = "UTC";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";

    protected String toISOFormatDate(Date date) {

        TimeZone tz = TimeZone.getTimeZone(UTC_TIMEZONE);
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        df.setTimeZone(tz);
        return df.format(date);
    }
}
