package com.nwai.dentalsys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwai.dentalsys.config.JWTService;
import com.nwai.dentalsys.dto.request.DentistRequestDto;
import com.nwai.dentalsys.dto.response.DentistResponseDto;
import com.nwai.dentalsys.service.DentistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DentistController.class)
class DentistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DentistService dentistService;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private DentistRequestDto dentistRequestDto;
    private DentistResponseDto dentistResponseDto;

    @BeforeEach
    void setup() {
        dentistRequestDto = new DentistRequestDto("Dr", "Strange", "1234567890","strange@clinic.com",  "Surgery");
        dentistResponseDto = new DentistResponseDto("Dr", "Strange", "1234567890","strange@clinic.com", "Surgery");
    }

    @Test
    @DisplayName("POST /dentists should create and return dentist")
    @WithMockUser
    void whenCreateDentist_thenReturnDentistResponse() throws Exception {
        when(dentistService.createDentist(dentistRequestDto)).thenReturn(dentistResponseDto);

        mockMvc.perform(post("/adsweb/api/v1/dentists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dentistRequestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dentistResponseDto)))
                .andDo(print());
    }

    @Test
    @DisplayName("Create dentist via service, then update via controller, and verify update")
    @WithMockUser
    void givenCreatedDentist_whenUpdate_thenReturnUpdatedDentistResponse() throws Exception {
        int dentistId = 1;

        // Step 1: Simulate "create" dentist via mocked service
        DentistRequestDto createRequest = new DentistRequestDto("Dr", "Strange", "1234567890", "strange@clinic.com", "Surgery");
        DentistResponseDto createdResponse = new DentistResponseDto("Dr", "Strange", "1234567890", "strange@clinic.com", "Surgery");

        when(dentistService.createDentist(createRequest)).thenReturn(createdResponse);
        DentistResponseDto created = dentistService.createDentist(createRequest);
        assertEquals("Strange", created.lastName());

        // Step 2: Prepare updated request
        DentistRequestDto updatedRequest = new DentistRequestDto("Dr", "House", "0987654321", "house@clinic.com", "Ortho");
        DentistResponseDto updatedResponse = new DentistResponseDto("Dr", "House", "0987654321", "house@clinic.com", "Ortho");

        when(dentistService.updateDentist(dentistId, updatedRequest)).thenReturn(updatedResponse);

        // Step 3: Perform PUT to update and assert
        mockMvc.perform(put("/adsweb/api/v1/dentists/{id}", dentistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("House"))
                .andExpect(jsonPath("$.email").value("house@clinic.com"))
                .andExpect(jsonPath("$.specialization").value("Ortho"))
                .andDo(print());

        // Optional verify
        verify(dentistService).updateDentist(dentistId, updatedRequest);
    }


    @Test
    @DisplayName("DELETE /dentists/{id} should delete and return no content")
    @WithMockUser
    void whenDeleteDentist_thenReturnNoContent() throws Exception {
        int dentistId = 1;

        mockMvc.perform(delete("/adsweb/api/v1/dentists/{id}", dentistId)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(dentistService).deleteDentist(dentistId);
    }
}