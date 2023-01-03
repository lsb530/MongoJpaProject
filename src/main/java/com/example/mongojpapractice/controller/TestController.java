package com.example.mongojpapractice.controller;

import com.example.mongojpalogic.guide.document.Planet;
import com.example.mongojpalogic.guide.document.Planet.SurfaceTemperatureC;
import com.example.mongojpalogic.guide.repository.GuideRepository;
import com.example.mongojpalogic.mflix.document.Users;
import com.example.mongojpalogic.mflix.repository.MflixUserUserRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Value;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {

    private final GuideRepository guideRepository;

    private final MflixUserUserRepository mflixUserUserRepository;

    @GetMapping("/api/test")
    public void testInsert() {
        Planet planet = new Planet();
        planet.setId(new ObjectId());
        planet.setName("테스트행성");
        planet.setOrderFromSun(10);
        planet.setHasRings(true);
        planet.setMainAtmosphere(List.of("HaHa", "HoHo"));
        SurfaceTemperatureC surfaceTemperatureC = new SurfaceTemperatureC();
        surfaceTemperatureC.setMax(10D);
        surfaceTemperatureC.setMax(1D);
        surfaceTemperatureC.setMean(3.4D);
        planet.setSurfaceTemperatureC(surfaceTemperatureC);
        this.guideRepository.save(planet);

        Users users = new Users();
        users.setId(new ObjectId());
        users.setName("테스트이름");
        users.setEmail("test" + RandomStringUtils.randomNumeric(1) + "@example.com");
        users.setPassword("testtest");
        this.mflixUserUserRepository.save(users);
    }

    @GetMapping("/api/test2")
    public void testBlockLog() throws JsonProcessingException {
        Map<String, String> user = new LinkedHashMap<>();
        user.put("user_id", "87656");
        user.put("SSN", "786445563");
        user.put("address", "22 Street");
        user.put("city", "Chicago");
        user.put("korean_name", "홍길동");
        user.put("Country", "U.S.");
        user.put("ip_address", "192.168.1.1");
        user.put("email_id", "spring@baeldung.com");
        JSONObject userDetails = new JSONObject(user);
        ObjectMapper objectMapper = new ObjectMapper();
        // InvalidDefinitionException: No serializer found for class
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        log.info(
            "User JSON: {}",
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails)
        );
    }
}
