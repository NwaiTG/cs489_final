package com.nwai.dentalsys;

import com.nwai.dentalsys.dto.request.AppointmentRequestDto;
import com.nwai.dentalsys.dto.response.AppointmentResponseDto;
import com.nwai.dentalsys.service.AppointmentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class DentalsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(DentalsysApplication.class, args);
    }

}
