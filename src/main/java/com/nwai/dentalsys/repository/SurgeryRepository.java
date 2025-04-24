package com.nwai.dentalsys.repository;

import com.nwai.dentalsys.model.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurgeryRepository extends JpaRepository<Surgery, Integer> {
    Optional<Surgery> findBySurgeryNo(String surgeryNo);
}
