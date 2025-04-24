package com.nwai.dentalsys.service.impl;

import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.request.SurgeryRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.dto.response.SurgeryResponseDto;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.mapper.SurgeryMapper;
import com.nwai.dentalsys.model.Address;
import com.nwai.dentalsys.model.Surgery;
import com.nwai.dentalsys.repository.SurgeryRepository;
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
class SurgeryServiceImplTest {

    @Mock
    private SurgeryRepository surgeryRepository;

    @Mock
    private SurgeryMapper surgeryMapper;

    @InjectMocks
    private SurgeryServiceImpl surgeryService;

    private Surgery surgery;
    private SurgeryRequestDto requestDto;
    private SurgeryResponseDto responseDto;
    private Address address;
    private AddressRequestDto addressRequestDto;
    private AddressResponseDto addressResponseDto;

    @BeforeEach
    void setUp() {
        address = new Address("No32", "StreetA", "CityA", "StateA", 11111);
        addressRequestDto = new AddressRequestDto("No32", "StreetA", "CityA", "StateA", 11111);
        addressResponseDto = new AddressResponseDto("No32", "StreetA", "CityA", "StateA", 11111);

        surgery = new Surgery("Sur001", "John");
        surgery.setAddress(address);

        requestDto = new SurgeryRequestDto("Sur001", "John", addressRequestDto);
        responseDto = new SurgeryResponseDto("Sur001", "John", addressResponseDto);
    }


    @Test
    @DisplayName("Create surgery and return response DTO")
    void givenSurgeryRequestDto_whenCreateSurgery_thenReturnResponseDto() {
        when(surgeryMapper.surgerRequestDtoToSurgery(requestDto)).thenReturn(surgery);
        when(surgeryRepository.save(surgery)).thenReturn(surgery);
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(responseDto);

        SurgeryResponseDto result = surgeryService.createSurgery(requestDto);

        assertEquals("Sur001", result.surgeryNo());
        assertEquals("John", result.surgeryName());
        verify(surgeryRepository).save(surgery);
    }

    @Test
    @DisplayName("Find surgery by surgeryNo and return response DTO")
    void givenSurgeryNo_whenFindSurgeryByNo_thenReturnResponseDto() {
        String surgeryNo = "Sur001";
        Address address = new Address("No32", "StreetA", "CityA", "StateA", 11111);
        AddressResponseDto addressResponseDto = new AddressResponseDto("No32", "StreetA", "CityA", "StateA",  11111);

        Surgery surgery = new Surgery("Sur001", "John");
        surgery.setAddress(address);
        SurgeryResponseDto responseDto = new SurgeryResponseDto("Sur001", "John", addressResponseDto);

        when(surgeryRepository.findBySurgeryNo(surgeryNo)).thenReturn(Optional.of(surgery));
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(responseDto);

        SurgeryResponseDto result = surgeryService.findBySurgeryNo(surgeryNo);

        assertEquals("Sur001", result.surgeryNo());
        assertEquals("John", result.surgeryName());
        assertEquals("StreetA", result.address().street());
    }

    @Test
    @DisplayName("Create surgery then update and verify updated info")
    void givenCreatedSurgery_whenUpdate_thenReturnUpdatedDto() {
        // Step 1: Mock create
        when(surgeryMapper.surgerRequestDtoToSurgery(requestDto)).thenReturn(surgery);
        when(surgeryRepository.save(surgery)).thenReturn(surgery);
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(responseDto);

        SurgeryResponseDto createdDto = surgeryService.createSurgery(requestDto);
        assertEquals("John", createdDto.surgeryName());

        // Step 2: Prepare update
        SurgeryRequestDto updatedRequest = new SurgeryRequestDto("Sur001", "Jane",
                new AddressRequestDto("202","NewStreet", "CityB", "StateB",  22222));

        when(surgeryRepository.findBySurgeryNo("Sur001")).thenReturn(Optional.of(surgery));

        surgery.setSurgeryNo("Sur001");
        surgery.setSurgeryName("Jane");
        surgery.getAddress().setCity("CityB");
        surgery.getAddress().setStreet("NewStreet");
        surgery.getAddress().setState("StateB");
        surgery.getAddress().setUnitNo("202");
        surgery.getAddress().setZip(22222);

        SurgeryResponseDto updatedDto = new SurgeryResponseDto("Sur001", "Jane",
                new AddressResponseDto("202","NewStreet", "CityB", "StateB",  22222));

        when(surgeryMapper.surgerRequestDtoToSurgery(updatedRequest)).thenReturn(surgery);
        when(surgeryRepository.save(any())).thenReturn(surgery);
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(updatedDto);

        SurgeryResponseDto result = surgeryService.updateSurgery("Sur001", updatedRequest);

        assertEquals("Sur001", result.surgeryNo());
        assertEquals("Jane", result.surgeryName());
        assertEquals("NewStreet", result.address().street());
    }

    @Test
    @DisplayName("Delete surgery by surgeryNo")
    void givenSurgeryNo_whenDeleteSurgery_thenVerifyRepositoryDelete() {
        String surgeryNo = "Sur001";
        when(surgeryRepository.findBySurgeryNo(surgeryNo)).thenReturn(Optional.of(surgery));
        surgeryService.deleteBySurgeryNo(surgeryNo);
        verify(surgeryRepository).findBySurgeryNo(surgeryNo);
    }

    @Test
    @DisplayName("Delete non-existent surgery should throw ResourceNotFoundException")
    void givenSurgeryNo_whenDeleteNonExistentSurgery_thenThrowResourceNotFound() {
        String surgeryNo = "Sur001";
        when(surgeryRepository.findBySurgeryNo(surgeryNo)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> surgeryService.deleteBySurgeryNo(surgeryNo));
        assertEquals(surgeryNo + " surgery no not found!", exception.getMessage());
    }
}