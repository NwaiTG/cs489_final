package com.nwai.dentalsys.dto.response;

import com.nwai.dentalsys.model.Appointment;

import java.util.List;

public record DentistWithAppointmentsResponseDto(
        DentistResponseDto dentist,
        List<AppointmentWithPatientResponseDto> appointments
) {
}
