package com.example.mongojpapractice.security.core;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CustomHttpSessionStrategy extends HeaderHttpSessionIdResolver {

    private static final String HEADER_X_AUTH_TOKEN = "x-auth-token";

    private static final String HEADER_AUTHENTICATION_INFO = "Authentication-Info";

    private final String headerName;

    /**
     * Convenience factory to create {@link HeaderHttpSessionIdResolver} that uses "X-Auth-AccessToken" header.
     *
     * @return the instance configured to use "X-Auth-AccessToken" header
     */
    public static HeaderHttpSessionIdResolver xAuthToken() {
        return new HeaderHttpSessionIdResolver(HEADER_X_AUTH_TOKEN);
    }

    /**
     * Convenience factory to create {@link HeaderHttpSessionIdResolver} that uses "Authentication-Info" header.
     *
     * @return the instance configured to use "Authentication-Info" header
     */
    public static HeaderHttpSessionIdResolver authenticationInfo() {
        return new HeaderHttpSessionIdResolver(HEADER_AUTHENTICATION_INFO);
    }

    /**
     * The name of the header to obtain the session id from.
     *
     * @param headerName the name of the header to obtain the session id from.
     */
    public CustomHttpSessionStrategy(String headerName) {
        super(headerName);
        this.headerName = headerName;
    }

    @Override
    public List resolveSessionIds(HttpServletRequest request) {
        String headerValue = request.getHeader(this.headerName);
        return headerValue != null ? Collections.singletonList(headerValue) : Collections.emptyList();
    }

    @Override
    public void setSessionId(
        HttpServletRequest request, HttpServletResponse response, String sessionId) {
        if (ObjectUtils.isEmpty(
            request.getSession().getAttribute("SPRING_SECURITY_CONTEXT"))) { // Security Context 가 Null 이면

            expireSession(request, response); // 로그인 실패니까, 임시로 생성된 세션까지 제거
            request.getSession().invalidate();
        } else {
            Cookie tokenCookie = new Cookie("X-AUTH-TOKEN", sessionId);
            tokenCookie.setPath("/");
            // add cookie to response
            response.addCookie(tokenCookie);

            response.setHeader(this.headerName, sessionId);
        }
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(this.headerName, "");
    }
}
