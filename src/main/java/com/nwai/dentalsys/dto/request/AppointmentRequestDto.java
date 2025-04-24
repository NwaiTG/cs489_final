package com.nwai.dentalsys.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestDto(

   @NotNull(message = "Appointment date should not be blank/empty/null")
   @JsonFormat(pattern = "yyyy-MM-dd")
   @FutureOrPresent(message = "Appointment date must be today or in the future")
   LocalDate appointmentDate,
   @NotNull(message = "Appointment time should not be blank/empty/null")
   LocalTime appointmentTime,
   String status,
   String confirmStatus,
   @NotNull(message = "Patient ID is required")
   int patientId,
   @NotNull(message = "Dentist should not be blank/empty/null")
   int dentistId,
   @NotNull(message = "Surgery should not be blank/empty/null")
   @Valid
   int surgeryId
) {
}
