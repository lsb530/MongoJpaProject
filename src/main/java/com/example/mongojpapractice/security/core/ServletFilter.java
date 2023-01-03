package com.example.mongojpapractice.security.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class ServletFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private boolean checkExclude(String value) {
        // 로그인 혹은 로그아웃, 등록(회원가입) URL 일 경우
        return (value.equals("143d85801cf356fb") // health-check
//            || value.equals("login")
            || value.equals("logout")
            || value.equals("find-passwd")
            || value.equals("register"));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURL().toString();

        try {
            URL aURL = new URL(path);

            String[] segments = aURL.getPath().split("/");

            if ((!(segments[3].isEmpty()) && this.checkExclude(segments[3]))) {

                return true;
            }

        } catch (MalformedURLException mue) {
            return false;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(response);

        ServletInputStream inputStream = httpServletRequest.getInputStream();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if ("{}".equals(body)) throw new RuntimeException("body must not be empty");
        log.info("필터 타는지 체크중");

        // 필터 적용
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        // 헤더 Content Type 설정
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.copyBodyToResponse();

    }
}
