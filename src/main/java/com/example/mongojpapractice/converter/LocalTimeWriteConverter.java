package com.example.mongojpapractice.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class LocalTimeWriteConverter implements Converter<LocalTime, Date> {

    @Override
    public Date convert(LocalTime localTime) {
        LocalDateTime ldt =
            LocalDateTime.of(
                1970, 1, 1, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        ZonedDateTime zdt = ldt.atZone(TimeZone.getDefault().toZoneId());
        return Date.from(zdt.toInstant());
    }
}
