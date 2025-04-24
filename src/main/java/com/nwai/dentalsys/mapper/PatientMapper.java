package com.nwai.dentalsys.mapper;

import org.mapstruct.*;

import java.util.List;

import com.nwai.dentalsys.model.Patient;
import com.nwai.dentalsys.dto.request.PatientRequestDto;
import com.nwai.dentalsys.dto.response.PatientResponseDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AddressMapper.class, AppointmentMapper.class} )
public interface PatientMapper {
    @Mappings({
            @Mapping(source = "address", target = "address")
    })
    Patient patientRequestDtoToPatient(PatientRequestDto patientRequestDto);

    @Mappings({
            @Mapping(source = "address", target = "address")
    })
    PatientResponseDto patientToPatientResponseDto(Patient patient);
}
