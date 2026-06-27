package com.pawar.auth.dto;

public class JwtResponseDto {

    private String token;
    private String refreshToken;
    private Long tokenExpiry;
    private String type = "Bearer";

    public JwtResponseDto() {
    }

    public JwtResponseDto(String token) {
        this.token = token;
    }

    public JwtResponseDto(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public JwtResponseDto(String token, String refreshToken, Long tokenExpiry) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpiry = tokenExpiry;
    }

    public JwtResponseDto(String token, String refreshToken, Long tokenExpiry, String type) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpiry = tokenExpiry;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(Long tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "JwtResponseDto [token=" + token + ", refreshToken=" + refreshToken + ", tokenExpiry=" + tokenExpiry
                + ", type=" + type + "]";
    }
}
