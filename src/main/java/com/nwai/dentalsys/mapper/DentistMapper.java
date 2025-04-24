package com.nwai.dentalsys.mapper;

import java.util.List;

import com.nwai.dentalsys.dto.request.DentistRequestDto;
import com.nwai.dentalsys.dto.response.DentistResponseDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.nwai.dentalsys.model.Dentist;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AppointmentMapper.class)
public interface DentistMapper {

    Dentist dentistRequestDtoToDentist(DentistRequestDto dentistRequestDto);

    DentistRequestDto dentistToDentistRequestDto(Dentist dentist);

    DentistResponseDto dentistToDentistResponseDto(Dentist dentist);

    List<DentistResponseDto> dentistToDentistResponseDtoList(List<Dentist> dentistList);

}
