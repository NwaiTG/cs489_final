package com.nwai.dentalsys.dto.response;

import com.nwai.dentalsys.model.Appointment;
import java.util.List;

public record DentistResponseDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String specialization
) {
}
