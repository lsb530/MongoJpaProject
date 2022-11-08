package com.example.mongojpapractice.controller;

import com.example.mongojpalogic.guide.document.Planet;
import com.example.mongojpalogic.guide.document.Planet.SurfaceTemperatureC;
import com.example.mongojpalogic.guide.repository.GuideRepository;
import com.example.mongojpalogic.mflix.document.Users;
import com.example.mongojpalogic.mflix.repository.MflixUserUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
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
}
