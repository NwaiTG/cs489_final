package com.nwai.dentalsys.service.impl;

import com.nwai.dentalsys.dto.request.DentistRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.dto.response.DentistResponseDto;
import com.nwai.dentalsys.exception.DataAlreadyExistedException;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.mapper.DentistMapper;
import com.nwai.dentalsys.model.Dentist;
import com.nwai.dentalsys.repository.DentistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DentistServiceImplTest {

    @Mock
    private DentistRepository dentistRepository;

    @Mock
    private DentistMapper dentistMapper;

    @InjectMocks
    private DentistServiceImpl dentistService;

    private DentistRequestDto dentistRequestDto;
    private Dentist dentist;
    private DentistResponseDto dentistResponseDto;

    @BeforeEach
    void setUp() {
        dentistRequestDto = new DentistRequestDto("Dr", "Strange", "strange@clinic.com", "1234567890", "Surgery");
        dentist = Dentist.builder()
                .firstName("Dr")
                .lastName("Strange")
                .email("strange@clinic.com")
                .phoneNumber("1234567890")
                .specialization("Surgery")
                .build();
        dentistResponseDto = new DentistResponseDto("Dr", "Strange", "strange@clinic.com", "1234567890", "Surgery");
    }

    @Test
    void givenValidDentist_whenCreate_thenReturnResponseDto() {
        when(dentistRepository.findByFirstNameAndLastName("Dr", "Strange")).thenReturn(Optional.empty());
        when(dentistMapper.dentistRequestDtoToDentist(dentistRequestDto)).thenReturn(dentist);
        when(dentistRepository.save(dentist)).thenReturn(dentist);
        when(dentistMapper.dentistToDentistResponseDto(dentist)).thenReturn(dentistResponseDto);

        DentistResponseDto result = dentistService.createDentist(dentistRequestDto);

        // then
        assertNotNull(result);
        assertEquals("Strange", result.lastName());
        verify(dentistRepository).save(dentist);
    }

    @Test
    void givenDuplicateDentist_whenCreate_thenThrowException() {
        when(dentistRepository.findByFirstNameAndLastName("Dr", "Strange")).thenReturn(Optional.empty());
        when(dentistMapper.dentistRequestDtoToDentist(dentistRequestDto)).thenReturn(dentist);
        when(dentistRepository.save(dentist)).thenReturn(dentist);
        when(dentistMapper.dentistToDentistResponseDto(dentist)).thenReturn(dentistResponseDto);
        when(dentistRepository.findByFirstNameAndLastName("Dr", "Strange")).thenReturn(Optional.of(dentist));

        DataAlreadyExistedException ex = assertThrows(DataAlreadyExistedException.class, () ->
                dentistService.createDentist(dentistRequestDto));

        assertEquals("Dentist name already existed!", ex.getMessage());
        verify(dentistRepository, never()).save(any());
    }

    @Test
    @DisplayName("Save dentist then update it and verify the updated result")
    void givenSavedDentist_whenUpdate_thenReturnUpdatedDentistDto() {
        // Step 1: Simulate initial save
//        Dentist savedDentist = new Dentist("Dr", "Strange", "strange@clinic.com", "1234567890", "Surgery");

        when(dentistRepository.findByFirstNameAndLastName("Dr", "Strange"))
                .thenReturn(Optional.empty());
        when(dentistMapper.dentistRequestDtoToDentist(dentistRequestDto)).thenReturn(dentist);
        when(dentistRepository.save(dentist)).thenReturn(dentist);
        when(dentistMapper.dentistToDentistResponseDto(dentist)).thenReturn(dentistResponseDto);

        DentistResponseDto created = dentistService.createDentist(dentistRequestDto);

        assertEquals("Strange", created.lastName());

        // Step 2: Prepare updated data
        DentistRequestDto updatedDto = new DentistRequestDto(
                "Dr", "House", "0987654321", "house@clinic.com",  "Orthodontics"
        );

        Dentist updatedDentist = new Dentist("Dr", "House", "0987654321","house@clinic.com",  "Orthodontics");
        DentistResponseDto updatedResponseDto = new DentistResponseDto("Dr", "House", "0987654321","house@clinic.com",  "Orthodontics");

        // Mock update
        when(dentistRepository.findById(1)).thenReturn(Optional.of(dentist));
        when(dentistRepository.save(any(Dentist.class))).thenReturn(updatedDentist);
        when(dentistMapper.dentistToDentistResponseDto(updatedDentist)).thenReturn(updatedResponseDto);

        // Step 3: Update dentist
        DentistResponseDto updated = dentistService.updateDentist(1, updatedDto);

        // Step 4: Assertions
        assertEquals("House", updated.lastName());
        assertEquals("house@clinic.com", updated.email());
        assertEquals("Orthodontics", updated.specialization());

        verify(dentistRepository).findById(1);
        verify(dentistRepository, times(2)).save(any(Dentist.class));
    }

    @Test
    @DisplayName("delete by dentist Id")
    void givenDentistId_whenDelete_thenRepositoryDeleteCalled() {
        when(dentistRepository.findById(1)).thenReturn(Optional.of(dentist));

        dentistService.deleteDentist(1);

        verify(dentistRepository).delete(dentist);
    }

    @Test
    void givenInvalidId_whenDelete_thenThrowNotFound() {
        when(dentistRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                dentistService.deleteDentist(1));

        assertEquals("Dentist not found!", ex.getMessage());
        verify(dentistRepository, never()).save(any());
    }



}