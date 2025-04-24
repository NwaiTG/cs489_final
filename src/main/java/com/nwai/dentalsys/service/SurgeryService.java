package com.nwai.dentalsys.service;

import com.nwai.dentalsys.dto.request.*;
import com.nwai.dentalsys.dto.response.*;
import java.util.Optional;
import java.util.List;

public interface SurgeryService {
    //Create
    SurgeryResponseDto createSurgery(SurgeryRequestDto surgeryRequestDto);

    //Update
    SurgeryResponseDto updateSurgery(String surgeryNo, SurgeryRequestDto surgeryRequestDto);

    //Find by isbn
    SurgeryResponseDto findBySurgeryNo(String surgeryNo);

    //Delete by isbn
    void deleteBySurgeryNo(String surgeryNo);

    //Find all
    List<SurgeryResponseDto> findAllSurgery();
}
