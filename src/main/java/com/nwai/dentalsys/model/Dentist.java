package com.nwai.dentalsys.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dentist")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dentist_id")
    private int id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String specialization;
    @OneToMany(mappedBy = "dentist")
    private List<Appointment> appointmentList;

    public Dentist(String firstName, String lastName, String phoneNumber, String email, String specialization){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.specialization = specialization;
    }
}
