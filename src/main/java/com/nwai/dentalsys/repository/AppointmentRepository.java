package com.nwai.dentalsys.repository;

import com.nwai.dentalsys.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    // Only one custom finder as requested
//    List<Appointment> findByStatus(String status);
    Page<Appointment> findBySurgery_surgeryName(String surgery, Pageable pageable);
    List<Appointment> findByDentist_Id(int dentistId);
    List<Appointment> findByPatient_Id(int patientId);
    Optional<Appointment> findByPatientIdAndAppointmentDateAndAppointmentTime(
            int patientId, LocalDate date, LocalTime time);
    int countByPatientId(int patientId);

}
