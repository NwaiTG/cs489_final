package com.nwai.dentalsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "surgery")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class Surgery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "surgery_id")
    private int id;
    @Column(nullable = false)
    private String surgeryNo;
    @Column(nullable = false)
    private String surgeryName;
    @OneToOne(cascade = CascadeType.PERSIST,  fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "surgery")
    private List<Appointment> appointmentList;

    public Surgery(String surgeryNo, String surgeryName){
        this.surgeryNo = surgeryNo;
        this.surgeryName = surgeryName;
    }

    public Surgery(String surgeryNo, String surgeryName, Address address) {
        this.surgeryNo = surgeryNo;
        this.surgeryName = surgeryName;
        this.address = address;
    }
}
