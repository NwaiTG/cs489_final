package com.nwai.dentalsys.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentWithDentistResponseDto(
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        String status,
        String confirmStatus,
        DentistResponseDto dentist,
        SurgeryResponseDto surgery
) {
}
