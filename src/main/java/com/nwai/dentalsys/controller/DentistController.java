package com.nwai.dentalsys.controller;

import com.nwai.dentalsys.dto.request.DentistRequestDto;
import com.nwai.dentalsys.dto.response.DentistResponseDto;
import com.nwai.dentalsys.service.DentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adsweb/api/v1/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;

    // 3. POST new dentist
    @PostMapping
    public ResponseEntity<DentistResponseDto> createDentist(@RequestBody DentistRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(dentistService.createDentist(requestDto));
    }

    // 4. PUT update dentist
    @PutMapping("/{dentistId}")
    public ResponseEntity<DentistResponseDto> updateDentist(@PathVariable int dentistId, @RequestBody DentistRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(dentistService.updateDentist(dentistId, requestDto));
    }

    // 5. DELETE dentist
    @DeleteMapping("/{dentistId}")
    public ResponseEntity<Void> deleteDentist(@PathVariable int dentistId) {
        dentistService.deleteDentist(dentistId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //no_content mean it's deleted
    }
}
