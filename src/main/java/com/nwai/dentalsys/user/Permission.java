package com.nwai.dentalsys.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_WRITE("admin:write"),
    ADMIN_READ("admin:read"),
    MEMBER_WRITE("member:write"),
    MEMBER_READ("member:read"),
    DENTIST_READ("dentist:read"),
    DENTIST_WRITE("dentist:write");

    @Getter
    private final String permission;
}
