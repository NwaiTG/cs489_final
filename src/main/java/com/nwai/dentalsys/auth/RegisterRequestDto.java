package com.nwai.dentalsys.auth;

import com.nwai.dentalsys.user.Role;

public record RegisterRequestDto(
        String firstName,
        String lastName,
        String username,
        String password,
        Role role

) {
}

///
//String firstName;
//    private String lastName;
//    private String username;
//    private String password;
// Role role;
///
