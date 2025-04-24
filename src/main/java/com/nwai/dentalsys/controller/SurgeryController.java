package com.nwai.dentalsys.controller;

import com.nwai.dentalsys.dto.request.SurgeryRequestDto;
import com.nwai.dentalsys.dto.response.SurgeryResponseDto;
import com.nwai.dentalsys.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adsweb/api/v1/surgerys")
@RequiredArgsConstructor
public class SurgeryController {
    private final SurgeryService surgeryService;
    // 3. POST new surgery
    @PostMapping
    public ResponseEntity<SurgeryResponseDto> createSurgery(@RequestBody SurgeryRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(surgeryService.createSurgery(requestDto));
    }

    // 4. PUT update surgery
    @PutMapping("/{surgeryNo}")
    public ResponseEntity<SurgeryResponseDto> updateSurgery(@PathVariable String surgeryNo, @RequestBody SurgeryRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(surgeryService.updateSurgery(surgeryNo, requestDto));
    }

    // 5. DELETE surgery
    @DeleteMapping("/{surgeryNo}")
    public ResponseEntity<Void> deleteSurgery(@PathVariable String surgeryNo) {
        surgeryService.deleteBySurgeryNo(surgeryNo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //no_content mean it's deleted
    }
}
