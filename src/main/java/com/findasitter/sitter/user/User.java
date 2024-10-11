package com.findasitter.sitter.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(unique = true)
        private Integer user_id;
        private String password;
        @NotEmpty
        private String user_fname;
        private @NotEmpty String user_lname;
        private @Email String user_emailaddress;
        private String user_phone;
        private String user_address;
        private String user_city;
        private String user_zip;

    public User(Integer user_id, String user_fname, String user_lname, String user_emailaddress,
                String user_phone, String user_address, String user_city, String user_zip) {
        this.user_id = user_id;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_emailaddress = user_emailaddress;
        this.user_phone = user_phone;
        this.user_address = user_address;
        this.user_city = user_city;
        this.user_zip = user_zip;
    }

    public User() {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
        public String getUsername() {
            return "";
        }

    // we might want to consider adding logic into these that would make sense for our own app
    // do we even want these?
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Integer getUserId() {
        return user_id;
    }

    public String getUserFname() {
        return user_fname;
    }

    public String getUserLname() {
        return user_lname;
    }

    public String getUserEmailaddress() {
        return user_emailaddress;
    }

    public String getUserPhone() {
        return user_phone;
    }

    public String getUserAddress() {
        return user_address;
    }

    public String getUserCity() {
        return user_city;
    }

    public String getUserZip() {
        return user_zip;
    }

    // Setters (if necessary)
    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public void setUserFname(String user_fname) {
        this.user_fname = user_fname;
    }

    public void setUserLname(String user_lname) {
        this.user_lname = user_lname;
    }

    public void setUserEmailaddress(String user_emailaddress) {
        this.user_emailaddress = user_emailaddress;
    }

    public void setUserPhone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUserAddress(String user_address) {
        this.user_address = user_address;
    }

    public void setUserCity(String user_city) {
        this.user_city = user_city;
    }

    public void setUserZip(String user_zip) {
        this.user_zip = user_zip;
    }

    // Optional: Override toString, equals, and hashCode methods if needed
    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_fname='" + user_fname + '\'' +
                ", user_lname='" + user_lname + '\'' +
                ", user_emailaddress='" + user_emailaddress + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_address='" + user_address + '\'' +
                ", user_city='" + user_city + '\'' +
                ", user_zip='" + user_zip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!user_id.equals(user.user_id)) return false;
        return user_emailaddress.equals(user.user_emailaddress);
    }

    @Override
    public int hashCode() {
        int result = user_id.hashCode();
        result = 31 * result + user_emailaddress.hashCode();
        return result;
    }
}



//package com.findasitter.sitter.user;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;
//
//public record User(
//        Integer user_id,
//        @NotEmpty String user_fname,
//        @NotEmpty String user_lname,
//        @Email String user_emailaddress,
//        String user_phone,
//        String user_address,
//        String user_city,
//        String user_zip
//){}