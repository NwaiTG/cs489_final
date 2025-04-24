package com.nwai.dentalsys.dto.request;

import com.nwai.dentalsys.model.*;
import java.util.List;

public record SurgeryRequestDto(
        String surgeryNo,
        String surgeryName,
        AddressRequestDto address
) {
}

/*
private String sugeryNo;
    @Column(nullable = false)
    private String sugeryName;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "sugery")
    private List<Appointment> appointmentList;
 */
