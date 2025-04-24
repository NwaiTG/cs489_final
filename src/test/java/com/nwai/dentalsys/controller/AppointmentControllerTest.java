package com.nwai.dentalsys.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwai.dentalsys.config.JWTService;
import com.nwai.dentalsys.dto.request.AppointmentRequestDto;
import com.nwai.dentalsys.dto.response.*;
import com.nwai.dentalsys.exception.DataAlreadyExistedException;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.service.AppointmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    // to simulate Httprequest and response
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private JWTService jwtService;

    // for body
    @Autowired
    private ObjectMapper objectMapper;

    private AddressResponseDto addressResponseDto =
            new AddressResponseDto(
                    "No111", "BB Street", "asfd", "LA", 9870
            );

    private AddressResponseDto addressResponseDto1 =
            new AddressResponseDto("No32", "AA Street", "Houston", "Texax", 1234);

    private PatientResponseDto patientResponseDto =
            new PatientResponseDto(
                    1,"Pat001",
                    "John", "Doe",
                    LocalDate.of(1990, 1, 1),"1234567890", "john.doe@gmail.com",
                    addressResponseDto
            );

    private DentistResponseDto dentistResponseDto =
            new DentistResponseDto(
                    "Dr", "Joe", "dr@gmail.com", "1234567890", "Operation"
            );

    private SurgeryResponseDto surgeryResponseDto =
            new SurgeryResponseDto("S001", "Sur John", addressResponseDto1);


    private AppointmentRequestDto appointmentRequestDto =
            new AppointmentRequestDto(
                    LocalDate.of(2025, 5, 1), LocalTime.of(14, 0), "ACTIVE", "CONFIRMED", 1, 1, 1
            );

    private AppointmentResponseDto appointmentResponseDto =
            new AppointmentResponseDto(
                    appointmentRequestDto.appointmentDate(),
                    appointmentRequestDto.appointmentTime(),
                    appointmentRequestDto.status(),
                    appointmentRequestDto.confirmStatus(),
                    patientResponseDto,
                    dentistResponseDto,
                    surgeryResponseDto
            );

    @Test
    @DisplayName("POST /appointments should create and return appointment")
    @WithMockUser
    void givenAppointmentRequestDto_whenCreate_thenReturnAppointmentResponseDto() throws Exception {
        // set mockito behavior for
        when(appointmentService.createAppointment(appointmentRequestDto)).thenReturn(appointmentResponseDto);

        // when (send post request) //create, put and pauch
        mockMvc.perform(
                MockMvcRequestBuilders.post("/adsweb/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequestDto))
                        .with(csrf())
        )
                //then
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(appointmentResponseDto)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Get /by-patient/{patientId} should return patient info with multiple appointments")
    @WithMockUser
    void givenPatientId_whenGetAllAppointmentsByPatientId_thenPrintPatientInfoAndAppointments() throws Exception {
        AppointmentRequestDto secondRequestDto = new AppointmentRequestDto(
                LocalDate.of(2025, 5, 2), LocalTime.of(10, 30),
                "COMPLETED", "CONFIRMED", 1, 1, 1
        );
        AppointmentWithDentistResponseDto firstAppointmentResponseDto = new AppointmentWithDentistResponseDto(
                appointmentRequestDto.appointmentDate(),
                appointmentRequestDto.appointmentTime(),
                appointmentRequestDto.status(),
                appointmentRequestDto.confirmStatus(),
                dentistResponseDto,
                surgeryResponseDto
        );

        AppointmentWithDentistResponseDto secondAppointmentResponseDto = new AppointmentWithDentistResponseDto(
                secondRequestDto.appointmentDate(),
                secondRequestDto.appointmentTime(),
                secondRequestDto.status(),
                secondRequestDto.confirmStatus(),
                dentistResponseDto,
                surgeryResponseDto
        );

        PatientWithAppointmentsResponseDto patientWithAppointmentsResponseDto = new PatientWithAppointmentsResponseDto(
                patientResponseDto,
                List.of(firstAppointmentResponseDto, secondAppointmentResponseDto)
        );

        when(appointmentService.getAppointmentsByPatientId(1)).thenReturn(patientWithAppointmentsResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/adsweb/api/v1/appointments/by-patient/{patientId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Patient assertions
                .andExpect(MockMvcResultMatchers.jsonPath("$.patient.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patient.email").value("john.doe@gmail.com"))
                // Appointments count
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments.length()").value(2))
                // Appointment 1
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[0].status").value("ACTIVE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[0].appointmentDate").value("2025-05-01"))
                // Appointment 2
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[1].status").value("COMPLETED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[1].appointmentDate").value("2025-05-02"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Get /by-patient/{patientId} should return patient info with multiple appointments")
    @WithMockUser
    void givenDentistId_whenGetAllAppointmentsByDentistId_thenPrintDentistInfoAndAppointments() throws Exception {
        AppointmentRequestDto secondRequestDto = new AppointmentRequestDto(
                LocalDate.of(2025, 5, 2), LocalTime.of(10, 30),
                "COMPLETED", "CONFIRMED", 1, 1, 1
        );
        AppointmentWithPatientResponseDto firstAppointmentResponseDto = new AppointmentWithPatientResponseDto(
                appointmentRequestDto.appointmentDate(),
                appointmentRequestDto.appointmentTime(),
                appointmentRequestDto.status(),
                appointmentRequestDto.confirmStatus(),
                patientResponseDto,
                surgeryResponseDto
        );

        AppointmentWithPatientResponseDto secondAppointmentResponseDto = new AppointmentWithPatientResponseDto(
                secondRequestDto.appointmentDate(),
                secondRequestDto.appointmentTime(),
                secondRequestDto.status(),
                secondRequestDto.confirmStatus(),
                patientResponseDto,
                surgeryResponseDto
        );

        DentistWithAppointmentsResponseDto dentistWithAppointmentsResponseDto = new DentistWithAppointmentsResponseDto(
                dentistResponseDto,
                List.of(firstAppointmentResponseDto, secondAppointmentResponseDto)
        );

        when(appointmentService.getAppointmentsByDentistId(1)).thenReturn(dentistWithAppointmentsResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/adsweb/api/v1/appointments/by-dentist/{dentistId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Patient assertions
                .andExpect(MockMvcResultMatchers.jsonPath("$.dentist.firstName").value("Dr"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dentist.lastName").value("Joe"))
                // Appointments count
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments.length()").value(2))
                // Appointment 1
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[0].status").value("ACTIVE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[0].appointmentDate").value("2025-05-01"))
                // Appointment 2
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[1].status").value("COMPLETED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointments[1].appointmentDate").value("2025-05-02"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("POST /appointments should return 409 CONFLICT if duplicate appointment exists")
    @WithMockUser
    void givenDuplicateAppointment_whenCreate_thenReturnConflict() throws Exception {
        // Given
        when(appointmentService.createAppointment(appointmentRequestDto))
                .thenThrow(new DataAlreadyExistedException("Patient already has an appointment at this date and time."));

        // When
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/adsweb/api/v1/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(appointmentRequestDto))
                                .with(csrf())
                )
                // Then
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Patient already has an appointment at this date and time."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("POST /appointments should return 404 if patient not found")
    @WithMockUser
    void givenNonExistentPatient_whenCreate_thenReturnNotFound() throws Exception {
        // Given
        when(appointmentService.createAppointment(appointmentRequestDto))
                .thenThrow(new ResourceNotFoundException("Patient not found"));

        // When
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/adsweb/api/v1/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(appointmentRequestDto))
                                .with(csrf())
                )
                // Then
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Patient not found"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("POST /appointments should return 404 if dentist not found")
    @WithMockUser
    void givenNonExistentDentist_whenCreate_thenReturnNotFound() throws Exception {
        // Given
        when(appointmentService.createAppointment(appointmentRequestDto))
                .thenThrow(new ResourceNotFoundException("Dentist not found"));

        // When
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/adsweb/api/v1/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(appointmentRequestDto))
                                .with(csrf())
                )
                // Then
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Dentist not found"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("POST /appointments should return 404 if surgery not found")
    @WithMockUser
    void givenNonExistentSurgery_whenCreate_thenReturnNotFound() throws Exception {
        // Given
        when(appointmentService.createAppointment(appointmentRequestDto))
                .thenThrow(new ResourceNotFoundException("Surgery not found"));

        // When
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/adsweb/api/v1/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(appointmentRequestDto))
                                .with(csrf())
                )
                // Then
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Surgery not found"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Create via service, then update via PUT, and return updated data")
    @WithMockUser
    void whenServiceCreatesAndControllerUpdatesAppointment_thenReturnUpdated() throws Exception {
        int appointmentId = 1;

        // Step 1: create appointment using service mock
        AppointmentRequestDto initialRequestDto = new AppointmentRequestDto(
                LocalDate.of(2025, 5, 1),
                LocalTime.of(14, 0),
                "ACTIVE", "CONFIRMED", 1, 1, 1
        );

        AppointmentResponseDto initialResponseDto = new AppointmentResponseDto(
                initialRequestDto.appointmentDate(),
                initialRequestDto.appointmentTime(),
                initialRequestDto.status(),
                initialRequestDto.confirmStatus(),
                patientResponseDto,
                dentistResponseDto,
                surgeryResponseDto
        );

        // Mock create call
        when(appointmentService.createAppointment(initialRequestDto)).thenReturn(initialResponseDto);

        // Simulate the service-level "creation"
        AppointmentResponseDto created = appointmentService.createAppointment(initialRequestDto);

        // Step 2: prepare update
        AppointmentRequestDto updatedRequestDto = new AppointmentRequestDto(
                LocalDate.of(2025, 6, 10),
                LocalTime.of(10, 0),
                "RESCHEDULED", "PENDING", 1, 1, 1
        );

        AppointmentResponseDto updatedResponseDto = new AppointmentResponseDto(
                updatedRequestDto.appointmentDate(),
                updatedRequestDto.appointmentTime(),
                updatedRequestDto.status(),
                updatedRequestDto.confirmStatus(),
                patientResponseDto,
                dentistResponseDto,
                surgeryResponseDto
        );

        // Mock update call
        when(appointmentService.updateAppointment(appointmentId, updatedRequestDto)).thenReturn(updatedResponseDto);

        // Step 3: test update via controller
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/adsweb/api/v1/appointments/{appointmentId}", appointmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedRequestDto))
                                .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointmentDate").value("2025-06-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointmentTime").value("10:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("RESCHEDULED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confirmStatus").value("PENDING"))
                .andDo(MockMvcResultHandlers.print());
    }

}