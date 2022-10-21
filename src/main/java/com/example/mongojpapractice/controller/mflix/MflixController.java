package com.example.mongojpapractice.controller.mflix;

import com.example.mongojpalogic.dto.TestDto;
import com.example.mongojpalogic.mflix.dto.MflixUserRes;
import com.example.mongojpalogic.mflix.service.MflixService;
import com.example.mongojpapractice.config.constants.StatusCodes;
import com.example.mongojpapractice.res.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MflixController {

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
