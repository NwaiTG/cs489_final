package com.nwai.dentalsys.dto.request;

public record AddressRequestDto (
        String unitNo,
        String street,
        String city,
        String state,
        Integer zip
){
}
