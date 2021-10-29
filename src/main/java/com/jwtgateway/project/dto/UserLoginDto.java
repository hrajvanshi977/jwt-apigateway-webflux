package com.jwtgateway.project.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String username;
    private String password;
}