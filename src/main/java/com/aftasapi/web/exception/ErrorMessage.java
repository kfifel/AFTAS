package com.aftasapi.web.exception;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorMessage extends Exception {

    private final int statusCode;
    private final Date timestamp;
    private final String message;
    private final String description;

    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}
