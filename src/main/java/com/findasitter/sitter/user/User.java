package com.findasitter.sitter.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class User {
    private Integer user_id;
    @NotEmpty
    private String user_fname;
    @NotEmpty
    private String user_lname;
    @Email
    private String user_emailaddress;
    private String user_phone;
    private String user_address;
    private String user_city;
    private String user_zip;
    private String user_password;

    public User(Integer user_id, String user_fname, String user_lname, String user_emailaddress, String user_phone, String user_address, String user_city, String user_zip, String user_password) {
        this.user_id = user_id;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_emailaddress = user_emailaddress;
        this.user_phone = user_phone;
        this.user_address = user_address;
        this.user_city = user_city;
        this.user_zip = user_zip;
        this.user_password = user_password;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_emailaddress() {
        return user_emailaddress;
    }

    public void setUser_emailaddress(String user_emailaddress) {
        this.user_emailaddress = user_emailaddress;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_zip() {
        return user_zip;
    }

    public void setUser_zip(String user_zip) {
        this.user_zip = user_zip;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}

