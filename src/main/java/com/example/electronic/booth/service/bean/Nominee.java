package com.example.electronic.booth.service.bean;

import jakarta.persistence.*;

public class Nominee {
    private Integer id;

    private String name;

    private String emailId;

    private String phone;

    private String party;

    private String city;

    private String state;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public Nominee() {}
    public Nominee(String name, String emailId, String phone, String party, String city, String state, String image) {
        this.name = name;
        this.emailId = emailId;
        this.phone = phone;
        this.party = party;
        this.city = city;
        this.state = state;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
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
}
