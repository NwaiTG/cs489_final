package com.nwai.dentalsys.mapper;

import org.mapstruct.*;

import com.nwai.dentalsys.model.Surgery;
import com.nwai.dentalsys.dto.request.SurgeryRequestDto;
import com.nwai.dentalsys.dto.response.SurgeryResponseDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AddressMapper.class, AppointmentMapper.class})
public interface SurgeryMapper {
    @Mappings({
            @Mapping(source = "address", target = "address"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "appointmentList", ignore = true)
    })
    Surgery surgerRequestDtoToSurgery(SurgeryRequestDto patientRequestDto);

    @Mappings({
            @Mapping(source = "address", target = "address"),
    })
    SurgeryResponseDto surgeryToSurgeryResponseDto(Surgery surgery);
}
