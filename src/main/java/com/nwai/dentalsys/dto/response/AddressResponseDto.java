package com.nwai.dentalsys.dto.response;

public record AddressResponseDto(
        String unitNo,
        String street,
        String city,
        String state,
        Integer zip
) {
}
