package com.nwai.dentalsys.service;

import com.nwai.dentalsys.dto.request.PatientRequestDto;
import com.nwai.dentalsys.dto.response.PatientResponseDto;

import java.util.List;
import java.util.Optional;

public interface PatientService {

//    List<PatientResponseDto> getAllPatientsSortedByLastName();

    Optional<PatientResponseDto> getPatientById(int id);

    PatientResponseDto createPatient(PatientRequestDto dto);

    Optional<PatientResponseDto> updatePatient(int id, PatientRequestDto dto);

    void deletePatientById(int id);
}
