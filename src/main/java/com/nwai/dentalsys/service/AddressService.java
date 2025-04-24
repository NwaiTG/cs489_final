package com.nwai.dentalsys.service;

import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    AddressResponseDto createAddress(AddressRequestDto addressRequestDto);
    AddressResponseDto updateAddress(int addressId, AddressRequestDto addressRequestDto);
    void  deleteAddress(int  addressId);
    List<AddressResponseDto> getAllAddressesSortedByCity();
}
