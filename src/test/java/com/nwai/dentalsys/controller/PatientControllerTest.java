package com.nwai.dentalsys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwai.dentalsys.dto.request.AddressRequestDto;
import com.nwai.dentalsys.dto.request.PatientRequestDto;
import com.nwai.dentalsys.dto.response.AddressResponseDto;
import com.nwai.dentalsys.dto.response.DentistResponseDto;
import com.nwai.dentalsys.dto.response.PatientResponseDto;
import com.nwai.dentalsys.service.PatientService;
import com.nwai.dentalsys.config.JWTService;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private PatientRequestDto requestDto;
    private PatientResponseDto responseDto;

    @BeforeEach
    void setUp() {
        AddressRequestDto addressDto = new AddressRequestDto("100", "Street", "City", "State", 12345);
        AddressResponseDto addressResponseDto = new AddressResponseDto("100", "Street", "City", "State", 12345);
        requestDto = new PatientRequestDto("P001", "Jane", "Smith", LocalDate.of(1985, 5, 5), "0987654321", "jane@example.com", addressDto);
        responseDto = new PatientResponseDto(1, "P001", "Jane", "Smith", LocalDate.of(1985, 5, 5), "0987654321", "jane@example.com", addressResponseDto);
    }

    @Test
    @WithMockUser
    @DisplayName("POST /patients should create and return patient")
    void givenPatientRequest_whenCreatePatient_thenReturnResponse() throws Exception {
        when(patientService.createPatient(any())).thenReturn(responseDto);

        mockMvc.perform(post("/adsweb/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patNo").value("P001"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /patients/{id} should return patient if found")
    void givenPatientId_whenGetPatientById_thenReturnPatient() throws Exception {
        when(patientService.getPatientById(1)).thenReturn(Optional.of(responseDto));

        mockMvc.perform(get("/adsweb/api/v1/patients/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patNo").value("P001"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /patients/{id} should return 404 if not found")
    void givenMissingPatientId_whenGetPatientById_thenReturn404() throws Exception {
        when(patientService.getPatientById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/adsweb/api/v1/patients/99")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /patients/{id} should update and return updated patient")
    void givenPatientRequest_whenUpdatePatient_thenReturnUpdatedPatient() throws Exception {
        int patientId = 1;

        AddressRequestDto updatedAddress = new AddressRequestDto("202", "NewStreet", "NewCity", "NewState", 54321);
        AddressResponseDto updatedAddressResponse = new AddressResponseDto("202", "NewStreet", "NewCity", "NewState", 54321);
        PatientRequestDto updatedDto = new PatientRequestDto("P001", "Janet", "Smith", LocalDate.of(1985, 5, 5), "9999999999", "janet@example.com", updatedAddress);
        PatientResponseDto updatedResponse = new PatientResponseDto(1,"P001", "Janet", "Smith", LocalDate.of(1985, 5, 5), "9999999999", "janet@example.com", updatedAddressResponse);

        when(patientService.createPatient(requestDto)).thenReturn(responseDto);
        PatientResponseDto created = patientService.createPatient(requestDto);
        assertEquals("Smith", created.lastName());

        when(patientService.updatePatient(patientId, updatedDto)).thenReturn(Optional.of(updatedResponse));

        mockMvc.perform(put("/adsweb/api/v1/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patNo").value("P001"))
                .andExpect(jsonPath("$.firstName").value("Janet"))
                .andExpect(jsonPath("$.phoneNumber").value("9999999999"))
                .andExpect(jsonPath("$.email").value("janet@example.com"))
                .andExpect(jsonPath("$.address.city").value("NewCity"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /patients/{id} should return 404 if patient not found")
    void givenMissingPatient_whenUpdatePatient_thenReturn404() throws Exception {
        int patientId = 3;
        when(patientService.updatePatient(patientId, requestDto)).thenReturn(Optional.ofNullable(responseDto));

        mockMvc.perform(put("/adsweb/api/v1/patients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /patients/{id} should delete patient")
    void givenPatientId_whenDeletePatient_thenReturnNoContent() throws Exception {
        int  patientId = 1;

        doNothing().when(patientService).deletePatientById(patientId);

        mockMvc.perform(delete("/adsweb/api/v1/patients/1").with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}