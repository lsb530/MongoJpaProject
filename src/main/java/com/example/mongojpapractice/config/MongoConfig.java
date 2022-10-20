package com.example.mongojpapractice.config;

import com.example.mongojpapractice.converter.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
class MongoConfig {

    @Bean
    MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        converterList.add(new ZonedDateTimeReadConverter());
        converterList.add(new ZonedDateTimeWriteConverter());
        converterList.add(new LocalTimeReadConverter());
        converterList.add(new LocalTimeWriteConverter());
        converterList.add(new BigDecimalReadConverter());
        converterList.add(new BigDecimalWriteConverter());
        converterList.add(new YearMonthReadConverter());
        converterList.add(new YearMonthWriteConverter());

        return new MongoCustomConversions(converterList);
    }

    /*
        1. ObjectId 가 timestamp 및 date로 파싱되는 것을 방지하기 위함
        2. Null 인 field는 반환되지 않도록 Global 처리
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.serializerByType(ObjectId.class, new ToStringSerializer());
    }
}
