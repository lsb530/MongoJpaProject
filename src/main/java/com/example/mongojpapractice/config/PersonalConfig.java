package com.example.mongojpapractice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "boki.personal-info")
@Component
@Data
public class PersonalConfig {
    private Integer age;
    private Integer height;
    private String name;
}
