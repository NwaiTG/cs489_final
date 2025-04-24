package com.nwai.dentalsys.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresss")
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private int id;
    @Column(nullable = false)
    private String unitNo;
    @Column(nullable = false)
    private String street;
//    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private Integer zip;

    public Address(String unitNo, String street, String city, String state, Integer zip) {
        this.unitNo = unitNo;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}
