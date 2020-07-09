package com.example.securityExample.dto;

public class LoginResponseDto {

    private String email;
    private String authToken;

    public LoginResponseDto(String email, String authToken) {
        this.email = email;
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
