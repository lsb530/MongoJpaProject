package com.example.mongojpapractice.security.exception;

import com.example.mongojpapractice.util.CommonUtils;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    private Random random = new Random();

    private int nextErrorSeq() {
        return this.random.nextInt(100000, 999999);
    }

    private void logDetailedException(int errSeq, Throwable ex,
        HttpServletRequest httpServletRequest, boolean enableStackTrace) {
        if (enableStackTrace) {
            log.error("""
                #errSeq: {},
                #user: {},
                #remoteAddr: {},
                #realRemoteAddr: {},
                #request: {},
                #headers: {},
                #exception: {}""",
                errSeq,
                httpServletRequest.getRemoteUser(),
                httpServletRequest.getRemoteAddr(),
                httpServletRequest.getHeader("x-forwarded-for") != null ?
                    httpServletRequest.getHeader("x-forwarded-for").split(",")[0].strip():
                    httpServletRequest.getRemoteAddr(),
                httpServletRequest.getRequestURI(),
                CommonUtils.getAllHeaders(httpServletRequest),
                CommonUtils.getStackTraceString(ex));
        }
        else {
            log.error("""
                #errSeq: {},
                #user: {},
                #remoteAddr: {},
                #realRemoteAddr: {},
                #request: {},
                #headers: {}""",
                errSeq,
                httpServletRequest.getRemoteUser(),
                httpServletRequest.getRemoteAddr(),
                httpServletRequest.getHeader("x-forwarded-for") != null ?
                    httpServletRequest.getHeader("x-forwarded-for").split(",")[0].strip():
                    httpServletRequest.getRemoteAddr(),
                httpServletRequest.getRequestURI(),
                CommonUtils.getAllHeaders(httpServletRequest));
        }

    }

    /**
     * JSON 데이터가 {} 으로 들어올 때 익셉션 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableHandling(HttpMessageNotReadableException ex, ServletWebRequest servletWebRequest){
        HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
        int errSeq = nextErrorSeq();
        logDetailedException(errSeq, ex, httpServletRequest, false);
//        ApiErrorResponse response = new ApiErrorResponse(StatusCodes.E010.name(), StatusCodes.E010.description);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Http Message is Not Readable");
    }
}
