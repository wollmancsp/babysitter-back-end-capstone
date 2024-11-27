package com.findasitter.sitter.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.websocket.Decoder;

import java.awt.*;
import java.sql.Blob;
import java.time.LocalDateTime;

public class User {
    private Integer user_id;
    private LocalDateTime user_created;
    @Email
    private String user_emailaddress;
    private String user_phone;
    @NotEmpty
    private String user_fname;
    @NotEmpty
    private String user_lname;
    private String user_address;
    private String user_city;
    private String user_zip;
    private String user_password;
    private Boolean user_role;
    private Boolean user_enabled;
    private String user_profilepicture;

    // Getters and setters for all fields
    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public LocalDateTime getUser_created() {
        return user_created;
    }

    public void setUser_created(LocalDateTime user_created) {
        this.user_created = user_created;
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

    public Boolean getUser_role() {
        return user_role;
    }

    public void setUser_role(Boolean user_role) {
        this.user_role = user_role;
    }

    public Boolean getUser_enabled() {
        return user_enabled;
    }

    public void setUser_enabled(Boolean user_enabled) {
        this.user_enabled = user_enabled;
    }
    public String getUser_profilepicture() {
        return user_profilepicture;
    }

    public void setUser_profilepicture(String user_profilepicture) {
        this.user_profilepicture = user_profilepicture;
    }
}
