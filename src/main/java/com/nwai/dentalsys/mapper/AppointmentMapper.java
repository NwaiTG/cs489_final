package com.nwai.dentalsys.mapper;

import com.nwai.dentalsys.dto.request.AppointmentRequestDto;
import com.nwai.dentalsys.dto.response.AppointmentResponseDto;
import com.nwai.dentalsys.dto.response.AppointmentWithDentistResponseDto;
import com.nwai.dentalsys.dto.response.AppointmentWithPatientResponseDto;
import com.nwai.dentalsys.model.Appointment;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
    Appointment appointmentRequestDtoToAppointment(AppointmentRequestDto appointmentRequestDto);
    AppointmentResponseDto appointmentToAppointmentResponseDto(Appointment appointment);

    @Mappings({
            @Mapping(source = "dentist", target = "dentist"),
            @Mapping(source = "surgery", target = "surgery")
    })
    AppointmentWithDentistResponseDto appointmentToAppointmentWithDentistResponseDto(Appointment appointment);

    AppointmentWithPatientResponseDto appointmentToAppointmentWithPatientResponseDto(Appointment appointment);
}
