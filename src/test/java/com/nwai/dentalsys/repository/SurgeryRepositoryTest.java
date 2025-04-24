package com.nwai.dentalsys.repository;

import com.nwai.dentalsys.model.Address;
import com.nwai.dentalsys.model.Surgery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SurgeryRepositoryTest {

    @Autowired
    private SurgeryRepository surgeryRepository;

    private Surgery surgery;

    @Test
    @DisplayName("Test for finding surgery with surgery number.")
    void givenSurgeryWithSurgeryNo_whenFindBySurgeryNo_thenReturnSurgery() {
        // Given
        Address address = new Address();
        address.setCity("Austin");
        address.setState("TX");
        address.setStreet("123 Main St");
        address.setUnitNo("1A");
        address.setZip(78701);

        Surgery surgery = new Surgery();
        surgery.setSurgeryNo("SX123");
        surgery.setSurgeryName("Dental House");
        surgery.setAddress(address);

        surgeryRepository.save(surgery);

        // When
        Optional<Surgery> result = surgeryRepository.findBySurgeryNo("SX123");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getSurgeryName()).isEqualTo("Dental House");
        assertThat(result.get().getSurgeryNo()).isEqualTo("SX123");
        assertThat(result.get().getAddress().getCity()).isEqualTo("Austin");
    }
}