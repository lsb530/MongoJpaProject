package com.example.mongojpapractice.config.res;

import com.example.mongojpapractice.constants.StatusCodes;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ApiResponse {

    private String code; // 동작 코드
    private String message; // 메시지
    private Object data; // 반환 데이터

    public ApiResponse() {}

    public ApiResponse(StatusCodes value) {
        this.code = value.name();
        this.message = value.description;
    }

    public ApiResponse(StatusCodes value, Object data) {
        this.code = value.name();
        this.message = value.description;
        this.data = data;
    }
}
