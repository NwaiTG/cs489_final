package com.nwai.dentalsys.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponseDto(
   LocalDate appointmentDate,
   LocalTime appointmentTime,
   String status,
   String confirmStatus,
   PatientResponseDto patient,
   DentistResponseDto dentist,
   SurgeryResponseDto surgery
) {
}
