package com.alinaberlin.ecommerceshop.payloads;

public class JwtAuthenticationResponse {

    private String token;

    public JwtAuthenticationResponse() {}

    public JwtAuthenticationResponse(String jwt) {
        this.token = jwt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
