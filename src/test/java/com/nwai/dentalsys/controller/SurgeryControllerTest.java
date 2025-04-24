package com.nwai.dentalsys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwai.dentalsys.config.JWTService;
import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.request.SurgeryRequestDto;
import com.nwai.dentalsys.dto.request.SurgeryRequestDto;
import com.nwai.dentalsys.dto.request.SurgeryRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.dto.response.SurgeryResponseDto;
import com.nwai.dentalsys.dto.response.SurgeryResponseDto;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.service.SurgeryService;
import com.nwai.dentalsys.service.SurgeryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SurgeryController.class)
class SurgeryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SurgeryService surgeryService;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private SurgeryRequestDto requestDto;
    private SurgeryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        AddressRequestDto addressDto = new AddressRequestDto("100", "Street", "City", "State", 12345);
        AddressResponseDto addressResponseDto = new AddressResponseDto("100", "Street", "City", "State", 12345);
        requestDto = new SurgeryRequestDto("Sur001", "Jane", addressDto);
        responseDto = new SurgeryResponseDto("Sur001", "Jane", addressResponseDto);
    }

    @Test
    @WithMockUser
    @DisplayName("POST /surgerys should create and return surgery")
    void givenSurgeryRequest_whenCreateSurgery_thenReturnResponse() throws Exception {
        when(surgeryService.createSurgery(any())).thenReturn(responseDto);

        mockMvc.perform(post("/adsweb/api/v1/surgerys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surgeryNo").value("Sur001"))
                .andExpect(jsonPath("$.surgeryName").value("Jane"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /surgerys/{surgerNo} should update and return updated surgery")
    void givenSurgeryRequest_whenUpdateSurgery_thenReturnUpdatedSurgery() throws Exception {
        String surgerNo = "Sur001";

        AddressRequestDto addressDto = new AddressRequestDto("100", "NewStreet", "City", "State", 12345);
        AddressResponseDto addressResponseDto = new AddressResponseDto("100", "NewStreet", "City", "State", 12345);

        SurgeryRequestDto updateRequestDto = new SurgeryRequestDto("Sur001", "Jane", addressDto);
        SurgeryResponseDto updateResponseDto = new SurgeryResponseDto("Sur001", "Jane", addressResponseDto);

        when(surgeryService.createSurgery(requestDto)).thenReturn(responseDto);
        SurgeryResponseDto created = surgeryService.createSurgery(requestDto);
        assertEquals("Sur001", created.surgeryNo());

        when(surgeryService.updateSurgery(surgerNo, updateRequestDto)).thenReturn(updateResponseDto);

        mockMvc.perform(put("/adsweb/api/v1/surgerys/{surgeryNo}", surgerNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surgeryNo").value("Sur001"))
                .andExpect(jsonPath("$.surgeryName").value("Jane"))
                .andExpect(jsonPath("$.address.street").value("NewStreet"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /surgerys/{surgeryNo} should return 404 if surgery not found")
    void givenMissingSurgery_whenUpdateSurgery_thenReturn404() throws Exception {
        String surgeryNo = "Sur002";
        when(surgeryService.updateSurgery(surgeryNo, requestDto))
                .thenThrow(new ResourceNotFoundException("Sur002 surgery no not found!"));

        mockMvc.perform(put("/adsweb/api/v1/surgerys/{surgeryNo}", surgeryNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                // Then
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Sur002 surgery no not found!"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /surgerys/{surgeryNo} should delete surgery")
    void givenSurgeryId_whenDeleteSurgery_thenReturnNoContent() throws Exception {
        String  surgeryNo = "Sur001";

        doNothing().when(surgeryService).deleteBySurgeryNo(surgeryNo);

        mockMvc.perform(delete("/adsweb/api/v1/surgerys/Sur001").with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}