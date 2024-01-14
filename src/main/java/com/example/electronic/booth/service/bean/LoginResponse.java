package com.example.electronic.booth.service.bean;

public class LoginResponse {
    String name;
    String email;
    String phone;
    String voterId;
    String username;
    String role;

    public LoginResponse(String name, String email, String phone, String voterId, String username, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.voterId = voterId;
        this.username = username;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
