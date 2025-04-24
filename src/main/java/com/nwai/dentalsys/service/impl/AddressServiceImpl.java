package com.nwai.dentalsys.service.impl;

import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.exception.DataAlreadyExistedException;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.mapper.AddressMapper;
import com.nwai.dentalsys.model.Address;
import com.nwai.dentalsys.repository.AddressRepository;
import com.nwai.dentalsys.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
        Optional<Address> existingAdd = addressRepository.findByUnitNo(addressRequestDto.unitNo());
        if (existingAdd.isPresent()) {
            throw new DataAlreadyExistedException("Address already existed!");
        }
        Address address = addressMapper.addressRequestDtoToAddress(addressRequestDto);

        Address savedAddress = addressRepository.save(address);;
        return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    @Override
    public AddressResponseDto updateAddress(int addressId, AddressRequestDto addressRequestDto) {
        Optional<Address> optionalAddress = addressRepository.findByUnitNo(addressRequestDto.unitNo());
        if (optionalAddress.isPresent()) {
            Address existed = optionalAddress.get();
            existed.setUnitNo(addressRequestDto.unitNo());
            existed.setStreet(addressRequestDto.street());
            existed.setCity(addressRequestDto.city());
            existed.setState(addressRequestDto.state());
            existed.setZip(addressRequestDto.zip());
            Address savedAddress = addressRepository.save(existed);;
            return addressMapper.addressToAddressResponseDto(savedAddress);
        }else{
            throw new ResourceNotFoundException("Address not found!");
        }
    }

    @Override
    public void deleteAddress(int addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isPresent()) {
            addressRepository.delete(optionalAddress.get());
        }else {
            throw new ResourceNotFoundException("Address not found!");
        }
    }

    @Override
    public List<AddressResponseDto> getAllAddressesSortedByCity() {
        return addressRepository.findAllByOrderByCityAsc()
                .stream()
                .map(addressMapper::addressToAddressResponseDto)
                .collect(Collectors.toList());
    }
}
