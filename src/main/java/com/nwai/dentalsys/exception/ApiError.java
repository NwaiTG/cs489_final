package com.nwai.dentalsys.exception;

import java.time.Instant;

public record ApiError (
        String message,
        String path,
        Integer statusCode,
        Instant timeStamp
){
}
