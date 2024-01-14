package com.example.electronic.booth.service.bean;

public class UserRequest {
    private String name;

    private String phone;

    private String emailId;

    private String voterId;

    private String username;
    private String password;

    private String city;

    private String state;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserRequest(String name, String phone, String emailId, String voterId, String username, String password, String city, String state, String image) {
        this.name = name;
        this.phone = phone;
        this.emailId = emailId;
        this.voterId = voterId;
        this.username = username;
        this.password = password;
        this.city = city;
        this.state = state;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserRequest() {
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
