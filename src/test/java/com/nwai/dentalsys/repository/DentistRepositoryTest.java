package com.nwai.dentalsys.repository;

import com.nwai.dentalsys.dto.response.DentistResponseDto;
import com.nwai.dentalsys.model.Address;
import com.nwai.dentalsys.model.Dentist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DentistRepositoryTest {

    @Autowired
    private DentistRepository dentistRepository;

    @Test
    @DisplayName("Given Dentist with name, When findByFirstNameAndLastName, Then return Dentist")
    void givenDentistName_whenFindByFirstNameAndLastName_thenReturnDentist() {
        // Given
        Dentist dentist = new Dentist();
        dentist.setFirstName("John");
        dentist.setLastName("Doe");
        dentist.setEmail("john.doe@example.com");
        dentist.setPhoneNumber("123-456-7890");
        dentist.setSpecialization("Orthodontics");

        dentistRepository.save(dentist);

        // When
        Optional<Dentist> result = dentistRepository.findByFirstNameAndLastName("John", "Doe");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.get().getSpecialization()).isEqualTo("Orthodontics");
    }

    @Test
    @DisplayName("Given no matching Dentist, When findByFirstNameAndLastName, Then return empty")
    void givenNoMatchingDentist_whenFindByFirstNameAndLastName_thenReturnEmpty() {
        // When
        Optional<Dentist> result = dentistRepository.findByFirstNameAndLastName("Jane", "Smith");

        // Then
        assertThat(result).isNotPresent();
    }
}