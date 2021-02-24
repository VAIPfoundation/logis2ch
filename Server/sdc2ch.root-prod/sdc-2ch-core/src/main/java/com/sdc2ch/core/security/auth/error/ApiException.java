package com.sdc2ch.core.security.auth.error;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@ToString
public class ApiException {
    
    private final int status;
    
    private final String message;
    private final String error;
    
    private final ErrorCode errorCode;
    private final Date timestamp;
    private final String path;

}
