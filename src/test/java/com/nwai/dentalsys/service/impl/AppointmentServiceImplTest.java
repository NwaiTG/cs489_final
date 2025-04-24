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
import com.nwai.dentalsys.model.*;
import com.nwai.dentalsys.repository.AppointmentRepository;
import com.nwai.dentalsys.repository.DentistRepository;
import com.nwai.dentalsys.repository.PatientRepository;
import com.nwai.dentalsys.repository.SurgeryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private SurgeryRepository surgeryRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private SurgeryMapper surgeryMapper;
    @Mock
    private DentistMapper dentistMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentServiceImpl;

    private Appointment appointment;
    private Surgery surgery;
    private Patient patient;
    private Dentist dentist;
    private Address address;
    private AppointmentRequestDto appointmentRequestDto;
    private AppointmentResponseDto appointmentResponseDto;
    private PatientResponseDto patientResponseDto;
    private DentistResponseDto dentistResponseDto;
    private SurgeryResponseDto surgeryResponseDto;
    private AddressResponseDto addressResponseDto;
    private AddressResponseDto addressResponseDto1;

    @BeforeEach
    void setUp() {
        // surgery
        surgery = Surgery.builder()
                .id(1)
                .surgeryName("Sur John")
                .surgeryNo("S001")
                .address(new Address("No32", "AA Street", "Houston", "Texax", 1234))
                .build();

        addressResponseDto1 = new AddressResponseDto("No32", "AA Street", "Houston", "Texax", 1234);

        surgeryResponseDto = new SurgeryResponseDto(
                "S001", "Sur John", addressResponseDto1
        );

        // patient
        patient = Patient.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .patNo("Pat001")
                .phoneNumber("1234567890")
                .dob(LocalDate.of(1990, 1, 1))
                .address(new Address("No111", "BB Street", "asfd", "LA", 9870))
                .build();

        // address
        address = new Address("No111", "BB Street", "asfd", "LA", 9870);

        // addressResponseDto
        addressResponseDto = new AddressResponseDto(
                "No111", "BB Street", "asfd", "LA", 9870
        );

        // PatientResponseDto
        patientResponseDto = new PatientResponseDto(
                1,"Pat001",
                "John", "Doe",
                LocalDate.of(1990, 1, 1),"1234567890", "john.doe@gmail.com",
                addressResponseDto
        );

        // dentist
        dentist = Dentist.builder()
                .id(1)
                .firstName("Dr")
                .lastName("Joe")
                .email("dr@gmail.com")
                .phoneNumber("1234567890")
                .specialization("Operation")
                .build();

        // dentistResponseDto
        dentistResponseDto = new DentistResponseDto(
                "Dr", "Joe", "dr@gmail.com", "1234567890", "Operation"
        );

        // appointment
        appointment = Appointment.builder()
                .appointmentDate(LocalDate.of(2025, 5, 1))
                .appointmentTime(LocalTime.of(14, 0))
                .status("ACTIVE")
                .confirmStatus("CONFIRMED")
                .surgery(surgery)
                .dentist(dentist)
                .patient(patient)
                .build();

        // appointmentRequestDto
        appointmentRequestDto = new AppointmentRequestDto(
                LocalDate.of(2025, 5, 1), LocalTime.of(14, 0), "ACTIVE", "CONFIRMED", 1, 1, 1);

        // build response DTO
                appointmentResponseDto = new AppointmentResponseDto(
                appointmentRequestDto.appointmentDate(),
                appointmentRequestDto.appointmentTime(),
                appointmentRequestDto.status(),
                appointmentRequestDto.confirmStatus(),
                patientResponseDto,
                dentistResponseDto,
                surgeryResponseDto
        );

    }

    @Test
    @DisplayName("Create appointment when duplicate exists")
    void whenDuplicateAppointment_thenThrowDataAlreadyExisted() {
        when(appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto)).thenReturn(appointment);
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(1, appointment.getAppointmentDate(), appointment.getAppointmentTime()))
                .thenReturn(Optional.of(appointment));

        DataAlreadyExistedException ex = assertThrows(DataAlreadyExistedException.class, () ->
                appointmentServiceImpl.createAppointment(appointmentRequestDto));

        assertEquals("Patient already has an appointment at this date and time.", ex.getMessage());
        verify(appointmentRepository, never()).save(any());

    }

    @Test
    @DisplayName("Create appointment when patient exceeds limit")
    void whenTooManyAppointments_thenThrowLimitExceeded() {
        when(appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto)).thenReturn(appointment);
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(1, appointment.getAppointmentDate(), appointment.getAppointmentTime()))
                .thenReturn(Optional.empty());
        when(appointmentRepository.countByPatientId(1)).thenReturn(6);

        LimitExceededException ex = assertThrows(LimitExceededException.class, () ->
                appointmentServiceImpl.createAppointment(appointmentRequestDto));

        assertEquals("Patient cannot have more than 5 appointments.", ex.getMessage());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create appointment when patient does not exist")
    void givenInvalidPatientId_whenCreate_thenThrowsResourceNotFoundException() {
        when(appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto)).thenReturn(appointment);
        when(patientRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentServiceImpl.createAppointment(appointmentRequestDto)
        );

        assertEquals("Patient not found", exception.getMessage());
        verify(patientRepository).findById(1);
        verify(appointmentRepository, Mockito.never())
                .save(any(Appointment.class));
    }

    @Test
    @DisplayName("Create appointment when dentist does not exist")
    void givenInvalidDentistId_whenCreate_thenThrowsResourceNotFoundException() {
        when(appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto)).thenReturn(appointment);
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(1, appointment.getAppointmentDate(), appointment.getAppointmentTime())).thenReturn(Optional.empty());
        when(appointmentRepository.countByPatientId(1)).thenReturn(0);
        when(dentistRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentServiceImpl.createAppointment(appointmentRequestDto)
        );

        assertEquals("Dentist not found", exception.getMessage());
        verify(dentistRepository).findById(1);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Create appointment when surgery not found")
    void whenSurgeryNotFound_thenThrowResourceNotFound() {
        when(appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto)).thenReturn(appointment);
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(1, appointment.getAppointmentDate(), appointment.getAppointmentTime()))
                .thenReturn(Optional.empty());
        when(appointmentRepository.countByPatientId(1)).thenReturn(0);
        when(dentistRepository.findById(1)).thenReturn(Optional.of(dentist));
        when(surgeryRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                appointmentServiceImpl.createAppointment(appointmentRequestDto));

        assertEquals("Surgery not found", ex.getMessage());
        verify(surgeryRepository).findById(1);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create appointment")
    void givenAppointmentRequestDto_whenCreate_thenReturnAppointmentResponseDto() {

        // set mockito behavior
        when(appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto)).thenReturn(appointment);
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(1, appointment.getAppointmentDate(), appointment.getAppointmentTime())).thenReturn(Optional.empty());
        when(appointmentRepository.countByPatientId(1)).thenReturn(0);
        when(dentistRepository.findById(1)).thenReturn(Optional.of(dentist));
        when(surgeryRepository.findById(1)).thenReturn(Optional.of(surgery));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.appointmentToAppointmentResponseDto(appointment)).thenReturn(appointmentResponseDto);

        // when
        AppointmentResponseDto result = appointmentServiceImpl.createAppointment(appointmentRequestDto);

        // then
        assertNotNull(result);
        assertEquals(appointmentResponseDto, result);
//        assertEquals(appointmentResponseDto.appointmentDate(), result.appointmentDate());
//        assertEquals(appointmentResponseDto.appointmentTime(), result.appointmentTime());
//        assertEquals(appointmentResponseDto.status(), result.status());
//        assertEquals(appointmentResponseDto.confirmStatus(), result.confirmStatus());
//        assertEquals(appointmentResponseDto.patient().id(), result.patient().id());
    }

    @Test
    @DisplayName("Delete appointment by ID")
    void givenId_whenDeleteAppointment_thenRepositoryCalled() {
        int appointmentId = 42;

        appointmentServiceImpl.deleteAppointment(appointmentId);

        verify(appointmentRepository, times(1)).deleteById(appointmentId);
    }

    @Test
    @DisplayName("Get appointments by dentist ID when none found")
    void givenDentistId_whenNoAppointments_thenThrowNotFound() {
        when(appointmentRepository.findByDentist_Id(1)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> appointmentServiceImpl.getAppointmentsByDentistId(1)
        );

        assertEquals("No appointments found for dentist ID: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Get appointments by dentist ID successfully")
    void givenDentistId_whenAppointmentsExist_thenReturnResponseDto() {
        List<Appointment> appointments = List.of(appointment); // previously initialized in setup

        when(appointmentRepository.findByDentist_Id(1)).thenReturn(appointments);
        when(dentistMapper.dentistToDentistResponseDto(dentist)).thenReturn(dentistResponseDto);
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(patientResponseDto);
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        DentistWithAppointmentsResponseDto response = appointmentServiceImpl.getAppointmentsByDentistId(1);

        assertNotNull(response);
        assertEquals("Dr", response.dentist().firstName());
        assertEquals(1, response.appointments().size());
        assertEquals("ACTIVE", response.appointments().get(0).status());
    }

    @Test
    @DisplayName("Get appointments by patient ID successfully")
    void givenPatientId_whenAppointmentsExist_thenReturnResponseDto() {
        List<Appointment> appointments = List.of(appointment);

        when(appointmentRepository.findByPatient_Id(1)).thenReturn(appointments);
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(patientResponseDto);
        when(dentistMapper.dentistToDentistResponseDto(dentist)).thenReturn(dentistResponseDto);
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        PatientWithAppointmentsResponseDto response = appointmentServiceImpl.getAppointmentsByPatientId(1);

        assertNotNull(response);
        assertEquals("John", response.patient().firstName());
        assertEquals(1, response.appointments().size());
        assertEquals("ACTIVE", response.appointments().get(0).status());
    }

}