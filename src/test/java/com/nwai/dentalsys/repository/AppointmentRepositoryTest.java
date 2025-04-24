package com.nwai.dentalsys.repository;

import com.nwai.dentalsys.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DentistRepository dentistRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SurgeryRepository surgeryRepository;

    private Dentist dentist;
    private Patient patient;
    private Surgery surgery;

    @BeforeEach
    void setUp() {
        Address address = new Address("State", "City", "Street", "Unit1", 12345);

        surgery = surgeryRepository.save(new Surgery("SURG123", "Main Surgery", address));
        dentist = dentistRepository.save(new Dentist("Dent", "Ist", "dentist@test.com", "1234567890", "GEN"));
        patient = patientRepository.save(new Patient("P001", "John", "Doe", LocalDate.of(1990, 1, 1), "234234234", "john@example.com", address));
    }


    @Test
    @DisplayName("given dentist ID, when findByDentist_Id, then returns appointments")
    void givenDentistId_whenFindByDentistId_thenReturnsAppointments() {
        Appointment appointment = appointmentRepository.save(new Appointment(LocalDate.now(), LocalTime.of(11, 0), "ACTIVE", "CONFIRMED", patient, dentist, surgery));

        List<Appointment> results = appointmentRepository.findByDentist_Id(dentist.getId());

        assertEquals(1, results.size());
        assertEquals("Dent", results.get(0).getDentist().getFirstName());
    }

    @Test
    @DisplayName("given patient ID, when findByPatient_Id, then returns appointments")
    void givenPatientId_whenFindByPatientId_thenReturnsAppointments() {
        Appointment appointment = appointmentRepository.save(new Appointment(LocalDate.now(), LocalTime.of(12, 0), "CONFIRMED", "ACTIVE", patient, dentist, surgery));

        List<Appointment> results = appointmentRepository.findByPatient_Id(patient.getId());

        assertEquals(1, results.size());
        Appointment result = results.get(0);
        assertEquals(LocalDate.now(), result.getAppointmentDate());
        assertEquals(LocalTime.of(12, 0), result.getAppointmentTime());
        assertEquals("ACTIVE", result.getConfirmStatus());
        assertEquals("CONFIRMED", result.getStatus());
        assertEquals("John", result.getPatient().getFirstName());
        assertEquals("Doe", result.getPatient().getLastName());
        assertEquals("Dent", result.getDentist().getFirstName());
        assertEquals("Ist", result.getDentist().getLastName());
        assertEquals("Main Surgery", result.getSurgery().getSurgeryName());
    }

    @Test
    @DisplayName("given patientId/date/time, when findByPatientIdAndDateAndTime, then returns optional appointment")
    void givenPatientDateTime_whenFindByPatientDateTime_thenReturnsAppointment() {
        Appointment appointment = appointmentRepository.save(new Appointment(LocalDate.now(), LocalTime.of(13, 0), "CONFIRMED", "ACTIVE", patient, dentist, surgery));

        Optional<Appointment> found = appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(
                patient.getId(), appointment.getAppointmentDate(), appointment.getAppointmentTime()
        );

        assertTrue(found.isPresent());
        assertEquals("ACTIVE", found.get().getConfirmStatus());
    }

    @Test
    @DisplayName("given patientId, when countByPatientId, then returns correct count")
    void givenPatientId_whenCountByPatientId_thenReturnsCount() {
        appointmentRepository.save(new Appointment(LocalDate.now(), LocalTime.of(14, 0), "CONFIRMED", "ACTIVE", patient, dentist, surgery));

        int count = appointmentRepository.countByPatientId(patient.getId());

        assertEquals(1, count);
    }

    @Test
    @DisplayName("given non-existent patient/date/time, when findByPatientIdAndDateAndTime, then returns empty")
    void givenInvalidPatientDateTime_whenFind_thenReturnsEmptyOptional() {
        Optional<Appointment> result = appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(
                9999, LocalDate.now(), LocalTime.of(15, 0)
        );

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("given invalid page, when findBySurgery_surgeryName, then returns empty page")
    void givenInvalidPage_whenFindBySurgerySurgeryName_thenReturnsEmpty() {
        appointmentRepository.save(new Appointment(LocalDate.now(), LocalTime.of(16, 0), "CONFIRMED", "ACTIVE", patient, dentist, surgery));

        Page<Appointment> page = appointmentRepository.findBySurgery_surgeryName("Main Surgery", PageRequest.of(1, 10));

        assertTrue(page.isEmpty());
    }

    @Test
    @DisplayName("given null fields, when save appointment, then throws DataIntegrityViolationException")
    void givenNullFields_whenSaveAppointment_thenThrowsException() {
        Appointment appointment = new Appointment();
        assertThrows(DataIntegrityViolationException.class, () -> {
            appointmentRepository.saveAndFlush(appointment);
        });
    }
}