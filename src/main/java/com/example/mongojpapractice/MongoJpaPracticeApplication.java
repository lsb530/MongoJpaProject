package com.example.mongojpapractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("com.example")
@ComponentScan("com.example")
@SpringBootApplication(scanBasePackages = {"com.example"})
public class MongoJpaPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoJpaPracticeApplication.class, args);
    }

}
