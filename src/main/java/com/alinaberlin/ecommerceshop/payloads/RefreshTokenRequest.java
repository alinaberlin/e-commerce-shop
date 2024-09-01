package com.alinaberlin.ecommerceshop.payloads;

import jakarta.validation.constraints.NotNull;

public class RefreshTokenRequest {

    @NotNull
    private String refreshToken;

    RefreshTokenRequest() {}

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
