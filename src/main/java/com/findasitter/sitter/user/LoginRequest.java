package com.findasitter.sitter.user;

public class LoginRequest {
    private String user_email;
    private String user_password;

    public String getEmail() {
        return user_email;
    }

    public void setEmail(String email) {
        this.user_email = email;
    }

    public String getPassword() {
        return user_password;
    }

    public void setPassword(String password) {
        this.user_password = password;
    }
}
