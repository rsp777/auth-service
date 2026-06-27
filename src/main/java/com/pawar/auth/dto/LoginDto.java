package com.pawar.auth.dto;

public class LoginDto {

    private String username;
    private String email;
    private String passwordHash;

    public LoginDto() {
    }

    public LoginDto(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public LoginDto(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "LoginDto [username=" + username + ", email=" + email + ", passwordHash=" + passwordHash + "]";
    }
}
