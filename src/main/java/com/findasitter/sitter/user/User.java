package com.findasitter.sitter.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record User(
    Integer user_id,
    @NotEmpty String user_fname,
    @NotEmpty String user_lname,
    @Email String user_emailaddress,
    String user_phone,
    String user_address,
    String user_city,
    String user_zip
        ){}