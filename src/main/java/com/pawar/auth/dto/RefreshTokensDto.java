package com.pawar.auth.dto;

import java.util.Date;

public class RefreshTokensDto {

    private Long id;
    private Long userId;
    private String token;
    private Date expires;
    private Date createdAt;
    private Boolean revoked;

    public RefreshTokensDto() {
    }

    public RefreshTokensDto(String token, Date expires) {
        this.token = token;
        this.expires = expires;
    }

    public RefreshTokensDto(String token, Date expires, Date createdAt, Boolean revoked) {
        this.token = token;
        this.expires = expires;
        this.createdAt = createdAt;
        this.revoked = revoked;
    }

    public RefreshTokensDto(Long id, Long userId, String token, Date expires, Date createdAt, Boolean revoked) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expires = expires;
        this.createdAt = createdAt;
        this.revoked = revoked;
    }

    public RefreshTokensDto(Long userId, String token, Date expires, Date createdAt) {
        this.userId = userId;
        this.token = token;
        this.expires = expires;
        this.createdAt = createdAt;
        this.revoked = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    @Override
    public String toString() {
        return "RefreshTokensDto [id=" + id + ", userId=" + userId + ", token=" + token + ", expires=" + expires
                + ", createdAt=" + createdAt + ", revoked=" + revoked + "]";
    }
}
