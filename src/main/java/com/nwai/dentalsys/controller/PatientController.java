package com.nwai.dentalsys.controller;

import com.nwai.dentalsys.dto.request.PatientRequestDto;
import com.nwai.dentalsys.dto.response.PatientResponseDto;
import com.nwai.dentalsys.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

//    // 1. GET all patients sorted by last name
//    @GetMapping
//    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
//        return ResponseEntity.ok(patientService.getAllPatientsSortedByLastName());
//    }

    // 2. GET a patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable int id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST new patient
    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody PatientRequestDto requestDto) {
        return ResponseEntity.ok(patientService.createPatient(requestDto));
    }

    // 4. PUT update patient
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable int id, @RequestBody PatientRequestDto requestDto) {
        return patientService.updatePatient(id, requestDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. DELETE patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable int id) {
        patientService.deletePatientById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //no_content mean it's deleted
    }

//    // 6. SEARCH patients
//    @GetMapping("/search/{searchString}")
//    public ResponseEntity<List<PatientResponseDto>> searchPatients(@PathVariable String searchString) {
//        return ResponseEntity.ok(patientService.searchPatients(searchString));
//    }
}
