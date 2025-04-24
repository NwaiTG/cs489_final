package com.nwai.dentalsys.dto.response;

import java.time.LocalDate;
import java.util.List;
import com.nwai.dentalsys.model.*;

public record PatientResponseDto(
        int id,
        String patNo,
        String firstName,
        String lastName,
        LocalDate dob,
        String phoneNumber,
        String email,
        AddressResponseDto address
) {
}
