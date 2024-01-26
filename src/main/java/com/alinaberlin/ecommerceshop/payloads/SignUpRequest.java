package com.alinaberlin.ecommerceshop.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

public class SignUpRequest {
    @Email
    private String username;
    @NotNull
    @Size(min = 4, max = 10)
    private String password;
    private String name;

    public SignUpRequest() {
    }

    public SignUpRequest(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
