//package com.example.mongojpapractice.security;
//
//import java.time.Duration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.session.data.mongo.JdkMongoSessionConverter;
//import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;
//
//@EnableMongoHttpSession
//public class MongoSessionConfig {
//    @Bean
//    public JdkMongoSessionConverter jdkMongoSessionConverter() {
//
////        return new JdkMongoSessionConverter(Duration.ofMinutes(30));
//        return new JdkMongoSessionConverter(Duration.ofSeconds(30));
//    }
//}
