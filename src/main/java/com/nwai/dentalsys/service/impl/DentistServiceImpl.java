package com.nwai.dentalsys.service.impl;

import com.nwai.dentalsys.dto.request.DentistRequestDto;
import com.nwai.dentalsys.dto.response.DentistResponseDto;
import com.nwai.dentalsys.exception.DataAlreadyExistedException;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.mapper.DentistMapper;
import com.nwai.dentalsys.model.Dentist;
import com.nwai.dentalsys.repository.DentistRepository;
import com.nwai.dentalsys.service.DentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {
    private final DentistRepository dentistRepository;
    private final DentistMapper dentistMapper;

    @Override
    public DentistResponseDto createDentist(DentistRequestDto dentistRequestDto) {
        Optional<Dentist> existingDen = dentistRepository.findByFirstNameAndLastName(dentistRequestDto.firstName(), dentistRequestDto.lastName());
        if (existingDen.isPresent()) {
            throw new DataAlreadyExistedException("Dentist name already existed!");
        }
        Dentist dentist = dentistMapper.dentistRequestDtoToDentist(dentistRequestDto);

        Dentist savedDentist = dentistRepository.save(dentist);;
        return dentistMapper.dentistToDentistResponseDto(savedDentist);
    }

    @Override
    public DentistResponseDto updateDentist(int dentistId, DentistRequestDto dentistRequestDto) {
        Optional<Dentist> optionalDentist = dentistRepository.findById(dentistId);
        if (optionalDentist.isPresent()) {
            Dentist existed = optionalDentist.get();
            existed.setFirstName(dentistRequestDto.firstName());
            existed.setLastName(dentistRequestDto.lastName());
            existed.setEmail(dentistRequestDto.email());
            existed.setPhoneNumber(dentistRequestDto.phoneNumber());
            existed.setSpecialization(dentistRequestDto.specialization());
            Dentist savedDentist = dentistRepository.save(existed);;
            return dentistMapper.dentistToDentistResponseDto(savedDentist);
        }else{
            throw new ResourceNotFoundException("Dentist not found!");
        }
    }

    @Override
    public void deleteDentist(int dentistId) {
        Optional<Dentist> optionalDentist = dentistRepository.findById(dentistId);
        if (optionalDentist.isPresent()) {
            dentistRepository.delete(optionalDentist.get());
        }else {
            throw new ResourceNotFoundException("Dentist not found!");
        }
    }

    @Override
    public List<DentistResponseDto> findAllDentist() {
        return dentistRepository.findAll()
                .stream()
                .map(dentistMapper::dentistToDentistResponseDto)
                .collect(Collectors.toList());
    }
}
