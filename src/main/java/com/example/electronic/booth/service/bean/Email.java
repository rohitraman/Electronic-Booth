package com.example.electronic.booth.service.bean;

public class Email {
    String emailId;
    String voterName;
    String candidateName;
    String party;
    String timestamp;
    String state;
    String city;

    public Email(String emailId, String voterName, String candidateName, String party, String timestamp, String state, String city) {
        this.emailId = emailId;
        this.voterName = voterName;
        this.candidateName = candidateName;
        this.party = party;
        this.timestamp = timestamp;
        this.state = state;
        this.city = city;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
