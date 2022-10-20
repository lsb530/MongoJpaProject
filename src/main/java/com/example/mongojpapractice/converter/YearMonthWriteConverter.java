package com.example.mongojpapractice.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class YearMonthWriteConverter implements Converter<YearMonth, String> {

    @Override
    public String convert(YearMonth yearMonth) {
        return yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
