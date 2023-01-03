package com.example.mongojpapractice.config.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LogbackHttpRequestFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//             가입된 기관의 사업자가 5개 이상일 경우 로깅을 태우지 않음
//            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestURI = servletRequest.getRequestURI();
//            if (requestURI.equals("/api/v2/143d85801cf356fb/health") || userDetails.getResources().size() >= 5) {
            if (requestURI.equals("/api/")) {
                return FilterReply.DENY;
            }
        }
        return FilterReply.ACCEPT;
    }
}
