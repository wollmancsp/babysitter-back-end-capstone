package com.findasitter.sitter.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    private String user_fname;
    private String user_lname;
    private String user_phone;
    private String user_address;
    private String user_city;
    private String user_zip;

    public User() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUser_fname() { return user_fname; }
    public void setUser_fname(String user_fname) { this.user_fname = user_fname; }

    public String getUser_lname() { return user_lname; }
    public void setUser_lname(String user_lname) { this.user_lname = user_lname; }

    public String getUser_phone() { return user_phone; }
    public void setUser_phone(String user_phone) { this.user_phone = user_phone; }

    public String getUser_address() { return user_address; }
    public void setUser_address(String user_address) { this.user_address = user_address; }

    public String getUser_city() { return user_city; }
    public void setUser_city(String user_city) { this.user_city = user_city; }

    public String getUser_zip() { return user_zip; }
    public void setUser_zip(String user_zip) { this.user_zip = user_zip; }

    // Implementing the UserDetails interface methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return user roles or authorities
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
//    public String user_emailaddress() {
//        return null;
//    }




//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;

//public class User {
//}
//
//
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