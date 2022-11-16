package com.example.mongojpapractice.controller.mflix;

import com.example.mongojpalogic.dto.TestDto;
import com.example.mongojpalogic.mflix.dto.CustomMflixUserRes;
import com.example.mongojpalogic.mflix.dto.MflixUserRes;
import com.example.mongojpalogic.mflix.service.MflixUserService;
import com.example.mongojpapractice.config.PersonalConfig;
import com.example.mongojpapractice.constants.StatusCodes;
import com.example.mongojpapractice.res.ApiResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/mflix")
@RestController
public class MflixUserController {
    private final MflixUserService mflixUserService;

    private final PersonalConfig personalConfig;

    @GetMapping("/session-check2")
    public ResponseEntity<Object> test(HttpSession session) {

        String sessionId = session.getId();

        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) session.getAttribute("data");

        Integer count;

        if(data == null) {
            count = 1;
        }
        else {
            count = (Integer) data.get("count");
            count++;
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("sessionId", sessionId);
        resultMap.put("count", count);

        session.setAttribute("data", resultMap);

        return ResponseEntity.ok(resultMap);
    }

    @GetMapping("/session-check")
    public ResponseEntity<Integer> count(HttpSession session) {

        Integer counter = (Integer) session.getAttribute("count");

        if (counter == null) {
            counter = 1;
        } else {
            counter++;
        }

        session.setAttribute("count", counter);

        return ResponseEntity.ok(counter);
    }

//    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(value = "/users")
    public ResponseEntity<List<MflixUserRes>> getMflixUsers() {
        System.out.println("personalConfig = " + personalConfig);
        return ResponseEntity.ok().body(mflixUserService.getMflixUsers());
    }

    @GetMapping(value = "/v2/user")
    public ResponseEntity<CustomMflixUserRes> getMflixUser() {
        List<MflixUserRes> mflixUsers = mflixUserService.getMflixUsers();
        return null;
    }

    @GetMapping(value = "agg/users")
    public ResponseEntity<Object> getMflixUsersAggregation(@RequestParam int age) {
        Object mflixUsersWithAge = mflixUserService.getMflixUsersWithAge(age);
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

    @GetMapping(value = "/test")
    public ResponseEntity<Object> justTest(
        @RequestParam int width,
        @RequestParam int height,
        @RequestParam float radius
    ) {
        TestDto testDto = new TestDto();
        testDto.setWidth(width);
        testDto.setHeight(height);
        ApiResponse apiResponse = new ApiResponse(StatusCodes.S001, testDto);
        return new ResponseEntity<>(apiResponse, StatusCodes.valueOf(apiResponse.getCode()).status);
    }
}
