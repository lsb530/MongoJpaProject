package com.example.mongojpapractice.constants;

import org.springframework.http.HttpStatus;

public enum StatusCodes {
    S001(HttpStatus.OK, "success"),
    S002(HttpStatus.CREATED, "created"),

    E001(HttpStatus.NO_CONTENT, "no content"),
    E002(HttpStatus.BAD_REQUEST, "bad request"),
    E003(HttpStatus.BAD_REQUEST, "bad service request");

    public final HttpStatus status;
    public final String description;

    StatusCodes(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
