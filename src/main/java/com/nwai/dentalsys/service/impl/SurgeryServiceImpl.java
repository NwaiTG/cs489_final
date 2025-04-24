package com.nwai.dentalsys.service.impl;

import com.nwai.dentalsys.dto.request.SurgeryRequestDto;
import com.nwai.dentalsys.dto.response.SurgeryResponseDto;
import com.nwai.dentalsys.exception.DataAlreadyExistedException;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.mapper.SurgeryMapper;
import com.nwai.dentalsys.model.Surgery;
import com.nwai.dentalsys.repository.SurgeryRepository;
import com.nwai.dentalsys.repository.SurgeryRepository;
import com.nwai.dentalsys.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurgeryServiceImpl implements SurgeryService {
    private final SurgeryRepository surgeryRepository;
    private final SurgeryMapper surgeryMapper;
    
    @Override
    public SurgeryResponseDto createSurgery(SurgeryRequestDto surgeryRequestDto) {
        Optional<Surgery> existingSur = surgeryRepository.findBySurgeryNo(surgeryRequestDto.surgeryNo());
        if (existingSur.isPresent()) {
            throw new DataAlreadyExistedException(surgeryRequestDto.surgeryNo() + " surgery no already existed");
        }

        Surgery surgery = surgeryMapper.surgerRequestDtoToSurgery(surgeryRequestDto);

        System.out.println("before saved surgery's address: " + surgery);

        Surgery savedSurgery = surgeryRepository.save(surgery);
        System.out.println("saved surgery's address: " + savedSurgery.getAddress());
        return surgeryMapper.surgeryToSurgeryResponseDto(savedSurgery);
    }

    @Override
    public SurgeryResponseDto updateSurgery(String surgeryNo, SurgeryRequestDto surgeryRequestDto) {
        Optional<Surgery> optionalSurgery = surgeryRepository.findBySurgeryNo(surgeryNo);
        if (optionalSurgery.isPresent()) {
            Surgery existed = optionalSurgery.get();
            Surgery mappedSurgery = surgeryMapper.surgerRequestDtoToSurgery(surgeryRequestDto);

            if (mappedSurgery.getAddress() != null) {
                mappedSurgery.getAddress().setId(existed.getAddress().getId());
            }
        }else{
            throw new ResourceNotFoundException(surgeryRequestDto.surgeryNo() + " surgery no not found!");
        }
        Surgery savedSurgery = surgeryRepository.save(surgeryMapper.surgerRequestDtoToSurgery(surgeryRequestDto));
        return surgeryMapper.surgeryToSurgeryResponseDto(savedSurgery);
    }

    @Override
    public SurgeryResponseDto findBySurgeryNo(String surgeryNo) {
        Surgery surgery = surgeryRepository.findBySurgeryNo(surgeryNo)
                .orElseThrow(() -> new ResourceNotFoundException(surgeryNo + " surgery no not found"));

        return surgeryMapper.surgeryToSurgeryResponseDto(surgery);
    }

    @Override
    public void deleteBySurgeryNo(String surgeryNo) {
        Optional<Surgery> optionalSurgery = surgeryRepository.findBySurgeryNo(surgeryNo);
        if (optionalSurgery.isPresent()) {
            surgeryRepository.delete(optionalSurgery.get());
        }else {
            throw new ResourceNotFoundException(surgeryNo + " surgery no not found!");
        }
    }

    @Override
    public List<SurgeryResponseDto> findAllSurgery() {
        return surgeryRepository.findAll()
                .stream()
                .map(surgeryMapper::surgeryToSurgeryResponseDto)
                .collect(Collectors.toList());
    }
}
