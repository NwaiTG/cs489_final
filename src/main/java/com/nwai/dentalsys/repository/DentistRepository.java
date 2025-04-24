package com.nwai.dentalsys.repository;

import com.nwai.dentalsys.model.Address;
import com.nwai.dentalsys.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Integer> {
//    Optional<Dentist> findByEmail(String email);

    Optional<Dentist> findByFirstNameAndLastName(String firstName, String lastName);
}
