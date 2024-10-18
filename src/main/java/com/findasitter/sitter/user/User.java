package com.findasitter.sitter.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record User(
    Integer user_id,
    LocalDateTime user_created,
    @Email String user_emailaddress,
    String user_phone,
    @NotEmpty String user_fname,
    @NotEmpty String user_lname,
   String user_address,
    String user_city,
    String user_zip,
    Integer parent_id,
    Integer sitter_id,
    String user_password
        ){}