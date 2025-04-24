package com.nwai.dentalsys.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PatientWithAppointmentsResponseDto(
        PatientResponseDto patient,
        List<AppointmentWithDentistResponseDto> appointments
) {
}
