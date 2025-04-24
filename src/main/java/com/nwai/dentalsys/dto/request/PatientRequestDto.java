package com.nwai.dentalsys.dto.request;

import java.util.List;
import java.time.LocalDate;
import com.nwai.dentalsys.model.*;

public record PatientRequestDto(
        String patNo,
        String firstName,
        String lastName,
        LocalDate dob,
        String phoneNumber,
        String email,
        AddressRequestDto address
) {
}
/*
private String patNo;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private java.time.LocalDate dob;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Integer email;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointmentList;
 */
