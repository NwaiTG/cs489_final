package com.nwai.dentalsys.service.impl;

import com.nwai.dentalsys.dto.request.AppointmentRequestDto;
import com.nwai.dentalsys.dto.response.*;
import com.nwai.dentalsys.exception.DataAlreadyExistedException;
import com.nwai.dentalsys.exception.LimitExceededException;
import com.nwai.dentalsys.exception.ResourceNotFoundException;
import com.nwai.dentalsys.mapper.AppointmentMapper;
import com.nwai.dentalsys.mapper.DentistMapper;
import com.nwai.dentalsys.mapper.PatientMapper;
import com.nwai.dentalsys.mapper.SurgeryMapper;
import com.nwai.dentalsys.model.Appointment;
import com.nwai.dentalsys.model.Patient;
import com.nwai.dentalsys.model.Dentist;
import com.nwai.dentalsys.model.Surgery;
import com.nwai.dentalsys.repository.AppointmentRepository;
import com.nwai.dentalsys.repository.PatientRepository;
import com.nwai.dentalsys.repository.DentistRepository;
import com.nwai.dentalsys.repository.SurgeryRepository;
import com.nwai.dentalsys.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final SurgeryRepository surgeryRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientMapper patientMapper;
    private final SurgeryMapper surgeryMapper;
    private final DentistMapper dentistMapper;

    public Page<AppointmentResponseDto> searchAppointment(int page, int pageSize, String sortDirection, String sortField) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortField
        );

        Page<Appointment> appointmentPage = appointmentRepository.findAll(pageable);
        return appointmentPage.map(app -> appointmentMapper.appointmentToAppointmentResponseDto(app));
    }

    @Override
    public Page<AppointmentResponseDto> searchAppointmentBySurgery(String surgery, int page, int pageSize, String sortDirection, String sortField) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortField
        );
        Page<Appointment> appointmentPage = appointmentRepository.findBySurgery_surgeryName(surgery, pageable);
        Page<AppointmentResponseDto> appointmentResponseDtoPage = appointmentPage.map(app -> appointmentMapper.appointmentToAppointmentResponseDto(app));
        return appointmentResponseDtoPage;
    }

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto dto) {
        Appointment appointment = appointmentMapper.appointmentRequestDtoToAppointment(dto);

        Patient patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Check duplicate for date & time
        Optional<Appointment> existing = appointmentRepository
                .findByPatientIdAndAppointmentDateAndAppointmentTime(dto.patientId(), dto.appointmentDate(), dto.appointmentTime());

        if (existing.isPresent()) {
            throw new DataAlreadyExistedException("Patient already has an appointment at this date and time.");
        }

        // Check count of appointments
        int count = appointmentRepository.countByPatientId(dto.patientId());

        if (count > 5) {
            throw new LimitExceededException("Patient cannot have more than 5 appointments.");
        }

        Dentist dentist = dentistRepository.findById(dto.dentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found"));
        Surgery surgery = surgeryRepository.findById(dto.surgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found"));

        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setSurgery(surgery);

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.appointmentToAppointmentResponseDto(saved);
    }

    @Override
    public AppointmentResponseDto updateAppointment(int id, AppointmentRequestDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setAppointmentDate(dto.appointmentDate());
        appointment.setAppointmentTime(dto.appointmentTime());
        appointment.setStatus(dto.status());
        appointment.setConfirmStatus(dto.confirmStatus());

        appointment.setPatient(patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found")));

        // Check duplicate for date & time
        Optional<Appointment> existing = appointmentRepository
                .findByPatientIdAndAppointmentDateAndAppointmentTime(dto.patientId(), dto.appointmentDate(), dto.appointmentTime());

        if (existing.isPresent()) {
            throw new DataAlreadyExistedException("Patient already has an appointment at this date and time.");
        }

        // Check count of appointments
        int count = appointmentRepository.countByPatientId(dto.patientId());

        if (count > 5) {
            throw new LimitExceededException("Patient cannot have more than 5 appointments.");
        }

        appointment.setDentist(dentistRepository.findById(dto.dentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found")));
        appointment.setSurgery(surgeryRepository.findById(dto.surgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found")));

        return appointmentMapper.appointmentToAppointmentResponseDto(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(int id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public AppointmentResponseDto getAppointmentById(int id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));

        return appointmentMapper.appointmentToAppointmentResponseDto(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::appointmentToAppointmentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public DentistWithAppointmentsResponseDto getAppointmentsByDentistId(int dentistId) {
        List<Appointment> appointments = appointmentRepository.findByDentist_Id(dentistId);

        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found for dentist ID: " + dentistId);
        }

        Dentist dentist = appointments.get(0).getDentist(); // all appointments have same dentist
        DentistResponseDto dentistResponseDto = dentistMapper.dentistToDentistResponseDto(dentist);

        List<AppointmentWithPatientResponseDto> appointmentDtos = appointments.stream()
                .map(appt -> new AppointmentWithPatientResponseDto(
                        appt.getAppointmentDate(),
                        appt.getAppointmentTime(),
                        appt.getStatus(),
                        appt.getConfirmStatus(),
                        patientMapper.patientToPatientResponseDto(appt.getPatient()),
                        surgeryMapper.surgeryToSurgeryResponseDto(appt.getSurgery())
                ))
                .collect(Collectors.toList());

        return new  DentistWithAppointmentsResponseDto(
                dentistResponseDto,
                appointmentDtos
        );
    }

    @Override
    public PatientWithAppointmentsResponseDto getAppointmentsByPatientId(int patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatient_Id(patientId);

        Patient patient = appointments.get(0).getPatient(); // all appointments have same patient
        PatientResponseDto patientDto = patientMapper.patientToPatientResponseDto(patient);

        List<AppointmentWithDentistResponseDto> appointmentDtos = appointments.stream()
                .map(appt -> new AppointmentWithDentistResponseDto(
                        appt.getAppointmentDate(),
                        appt.getAppointmentTime(),
                        appt.getStatus(),
                        appt.getConfirmStatus(),
                        dentistMapper.dentistToDentistResponseDto(appt.getDentist()),
                        surgeryMapper.surgeryToSurgeryResponseDto(appt.getSurgery())
                ))
                .collect(Collectors.toList());

        return new  PatientWithAppointmentsResponseDto(
                patientDto,
                appointmentDtos
        );
    }
}
