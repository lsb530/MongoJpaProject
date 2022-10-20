package com.example.mongojpapractice.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class LocalTimeReadConverter implements Converter<Date, LocalTime> {

    @Override
    public LocalTime convert(Date date) {
        ZonedDateTime zdt = date.toInstant().atZone(TimeZone.getDefault().toZoneId());
        return LocalTime.of(zdt.getHour(), zdt.getMinute(), zdt.getSecond());
    }
}
