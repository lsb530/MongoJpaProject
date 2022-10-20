package com.example.mongojpapractice.converter;

import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class YearMonthReadConverter implements Converter<String, YearMonth> {

    @Override
    public YearMonth convert(@NotNull String yearMonth) {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
