package com.example.mongojpapractice.controller;

import static com.example.mongojpapractice.util.MessageUtil.sendSlackMessage;

import com.example.mongojpalogic.auth.LoginReq;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONStringer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    ObjectMapper mapper;

    @PostMapping("/login")
    public void login(@RequestBody LoginReq loginReq, HttpServletResponse response) {
        UserDetails userDetails = null;
        if (ObjectUtils.isEmpty(loginReq)) throw new RuntimeException("로그인 관련 데이터가 비어있음");
        else if(loginReq.getId().equals("user") && loginReq.getPassword().equals("password")) {
            userDetails = inMemoryUserDetailsManager.loadUserByUsername("user");
        }
        else if(loginReq.getId().equals("admin") && loginReq.getPassword().equals("admin")) {
            userDetails = inMemoryUserDetailsManager.loadUserByUsername("admin");
        }
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(),
                    userDetails.getAuthorities()
                );

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);
            sendSlackMessage(String.format("아이디 [%s] 사용자 사이트에 로그인했습니다",userDetails.getUsername()));
            log.info("로그인 성공");
        }
        else {
            log.info("로그인 실패");
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("cookie.getName() = " + cookie.getName());
            System.out.println("cookie.getValue() = " + cookie.getValue());
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("DisplayName: %s, ID: %s, Offset: %s%n",
            TimeZone.getDefault().getDisplayName(),
            TimeZone.getDefault().getID(),
            TimeZone.getDefault().getRawOffset());
        System.out.println("------------------------------------------------------------------------------------------");
        SecurityContextHolder.clearContext();
    }
}
