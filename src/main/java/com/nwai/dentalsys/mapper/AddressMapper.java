package com.nwai.dentalsys.mapper;

import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.model.Address;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    Address addressRequestDtoToAddress(AddressRequestDto addressRequestDto);

    AddressResponseDto addressToAddressResponseDto(Address address);

    //map from Address to response
    AddressRequestDto addressToAddressRequestDto(Address address);
}
