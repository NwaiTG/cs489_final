package com.nwai.dentalsys.dto.request;

import com.nwai.dentalsys.model.Appointment;
import java.util.List;

public record DentistRequestDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String specialization
) {
}

