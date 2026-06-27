package com.pawar.auth.dto;

import java.util.Date;

public class VerificationTokenDto {

    private Long id;
    private String token;
    private Date expiryDate;
    private Boolean verified;
    private Long userId;

    public VerificationTokenDto() {
    }

    public VerificationTokenDto(String token, Date expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.verified = false;
    }

    public VerificationTokenDto(String token, Date expiryDate, Boolean verified) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.verified = verified;
    }

    public VerificationTokenDto(Long userId, String token, Date expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
        this.verified = false;
    }

    public VerificationTokenDto(Long id, String token, Date expiryDate, Boolean verified, Long userId) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.verified = verified;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VerificationTokenDto [id=" + id + ", token=" + token + ", expiryDate=" + expiryDate
                + ", verified=" + verified + ", userId=" + userId + "]";
    }
}
