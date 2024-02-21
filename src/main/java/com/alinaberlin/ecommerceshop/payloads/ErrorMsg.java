package com.alinaberlin.ecommerceshop.payloads;

public record ErrorMsg(String status, String message) {

    public ErrorMsg {
    }
}
