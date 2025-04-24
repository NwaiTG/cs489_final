package com.nwai.dentalsys.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
