package com.nwai.dentalsys.service;

import com.nwai.dentalsys.dto.request.*;
import com.nwai.dentalsys.dto.response.*;
import java.util.Optional;
import java.util.List;

public interface DentistService {
    //Create
    DentistResponseDto createDentist(DentistRequestDto dentistRequestDto);

    //Update
    DentistResponseDto updateDentist(int dentistId, DentistRequestDto dentistRequestDto);

    //delete
    void deleteDentist(int dentistId);

    //Find all
    List<DentistResponseDto> findAllDentist();
}
