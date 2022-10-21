package com.example.mongojpapractice.mflix.controller;

import com.example.mongojpalogic.dto.TestDto;
import com.example.mongojpapractice.config.res.ApiResponse;
import com.example.mongojpapractice.constants.StatusCodes;
import com.example.mongojpapractice.mflix.dto.MflixUserRes;
import com.example.mongojpapractice.mflix.service.MflixService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MongoController {

    private final MflixService mflixService;

    @GetMapping(value = "/sample-mflix/users")
    public ResponseEntity<List<MflixUserRes>> getMflixUsers() {
        return ResponseEntity.ok().body(mflixService.getMflixUsers());
    }

    @GetMapping(value = "agg/sample-mflix/users")
    public ResponseEntity<Object> getMflixUsersAggregation(@RequestParam int age) {
        Object mflixUsersWithAge = mflixService.getMflixUsersWithAge(age);
        ApiResponse apiResponse = new ApiResponse(StatusCodes.S001, mflixUsersWithAge);
        return new ResponseEntity<>(apiResponse, StatusCodes.valueOf(apiResponse.getCode()).status);
    }

    @GetMapping(value = "/multi-module-test")
    public ResponseEntity<Object> getRectangle(@RequestParam int width, @RequestParam int height) {
        TestDto testDto = new TestDto();
        testDto.setWidth(width);
        testDto.setHeight(height);
        ApiResponse apiResponse = new ApiResponse(StatusCodes.S001, testDto);
        return new ResponseEntity<>(apiResponse, StatusCodes.valueOf(apiResponse.getCode()).status);
    }
}
