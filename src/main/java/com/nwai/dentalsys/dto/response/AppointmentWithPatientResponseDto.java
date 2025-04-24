package com.nwai.dentalsys.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentWithPatientResponseDto(
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        String status,
        String confirmStatus,
        PatientResponseDto patient,
        SurgeryResponseDto surgery
) {
}
