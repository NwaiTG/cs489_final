package com.nwai.dentalsys.service.impl;

import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.request.PatientRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.dto.response.PatientResponseDto;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.mapper.PatientMapper;
import com.nwai.dentalsys.model.Address;
import com.nwai.dentalsys.model.Patient;
import com.nwai.dentalsys.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientRequestDto requestDto;
    private PatientResponseDto responseDto;
    private Address address;
    private AddressRequestDto addressRequestDto;
    private AddressResponseDto addressResponseDto;

    @BeforeEach
    void setUp() {
        address = new Address("No32", "StreetA", "CityA", "StateA", 11111);
        addressRequestDto = new AddressRequestDto("No32", "StreetA", "CityA", "StateA", 11111);
        addressResponseDto = new AddressResponseDto("No32", "StreetA", "CityA", "StateA", 11111);

        patient = new Patient("P001", "John", "Doe", LocalDate.parse("1990-01-01"), "1234567890", "john@example.com");
        patient.setAddress(address);

        requestDto = new PatientRequestDto("P001", "John", "Doe", LocalDate.of(1990, 1, 1), "1234567890", "john@example.com", addressRequestDto);
        responseDto = new PatientResponseDto(1,"P001", "John", "Doe", LocalDate.of(1990, 1, 1), "1234567890", "john@example.com", addressResponseDto);
    }


    @Test
    @DisplayName("Create patient and return response DTO")
    void givenPatientRequestDto_whenCreatePatient_thenReturnResponseDto() {
        when(patientMapper.patientRequestDtoToPatient(requestDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(responseDto);

        PatientResponseDto result = patientService.createPatient(requestDto);

        assertEquals("John", result.firstName());
        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("Find patient by ID and return response DTO")
    void givenPatientId_whenFindPatientById_thenReturnResponseDto() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(responseDto);

        Optional<PatientResponseDto> result = patientService.getPatientById(1);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().firstName());
    }

    @Test
    @DisplayName("Create patient then update and verify updated info")
    void givenCreatedPatient_whenUpdate_thenReturnUpdatedDto() {
        // Step 1: Mock create
        when(patientMapper.patientRequestDtoToPatient(requestDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(responseDto);

        PatientResponseDto createdDto = patientService.createPatient(requestDto);
        assertEquals("John", createdDto.firstName());

        // Step 2: Prepare update
        PatientRequestDto updatedRequest = new PatientRequestDto("P001", "Jane", "Smith", LocalDate.of(1985, 5, 5), "0987654321", "jane@example.com",
                new AddressRequestDto("CityB", "StreetB", "StateB", "202", 22222));

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

        patient.setFirstName("Jane");
        patient.setLastName("Smith");
        patient.setDob(LocalDate.parse("1985-05-05"));
        patient.setPhoneNumber("0987654321");
        patient.setEmail("jane@example.com");
        patient.getAddress().setCity("CityB");
        patient.getAddress().setStreet("StreetB");
        patient.getAddress().setState("StateB");
        patient.getAddress().setUnitNo("202");
        patient.getAddress().setZip(22222);

        PatientResponseDto updatedDto = new PatientResponseDto(1, "P001", "Jane", "Smith", LocalDate.of(1985, 5, 5),  "0987654321", "jane@example.com",
                new AddressResponseDto("202", "StreetB", "CityB", "StateB", 22222));

        when(patientRepository.save(any())).thenReturn(patient);
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(updatedDto);

        Optional<PatientResponseDto> result = patientService.updatePatient(1, updatedRequest);

        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().firstName());
        assertEquals("CityB", result.get().address().city());
    }

    @Test
    @DisplayName("Delete patient by ID")
    void whenDeletePatient_thenVerifyRepositoryDelete() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        patientService.deletePatientById(1);
        verify(patientRepository).deleteById(1);
    }

    @Test
    @DisplayName("Delete non-existent patient should throw ResourceNotFoundException")
    void givenPatientId_whenDeleteNonExistentPatient_thenThrowResourceNotFound() {
        when(patientRepository.findById(99)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatientById(99));
        assertEquals("Patient not found for  id: 99", exception.getMessage());
    }
}