package com.dhht.model;

public class UserDomain {
    private String username;
    private String  password;
    private Integer caNum;

    public UserDomain(String username, String password,Integer caNum) {
        this.username = username;
        this.password = password;
        this.caNum = caNum;
    }
    public UserDomain() {

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

    public Integer getCaNum() {
        return caNum;
    }

    public void setCaNum(Integer caNum) {
        this.caNum = caNum;
    }
}