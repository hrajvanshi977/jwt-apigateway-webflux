package com.jwtgateway.project.service.security;

import com.jwtgateway.project.app.ApiException;

public class AuthException extends ApiException {
    public AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}