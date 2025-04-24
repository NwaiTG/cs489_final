package com.nwai.dentalsys.dto.response;

import com.nwai.dentalsys.model.*;
import java.util.List;

public record SurgeryResponseDto(
        String surgeryNo,
        String surgeryName,
        AddressResponseDto address
) {
}
