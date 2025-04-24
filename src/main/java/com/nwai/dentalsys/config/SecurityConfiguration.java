package com.nwai.dentalsys.config;

import com.nwai.dentalsys.user.Permission;
import com.nwai.dentalsys.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    //to permit register and login form for all user
    //whilelist for URL
    @Bean
    public SecurityFilterChain securyityFilterChain(HttpSecurity http) throws Exception {
        http.
                csrf(CsrfConfigurer::disable) //disable csrf
                .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                        .requestMatchers("/adsweb/api/v1/auth/*").permitAll()

                        // Appointments
                        .requestMatchers("/adsweb/api/v1/appointments/by-surgery/").hasRole("ADMIN")
                        .requestMatchers("/adsweb/api/v1/appointments/search").hasRole("ADMIN")
                        .requestMatchers("/adsweb/api/v1/appointments/by-patient/**").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers("/adsweb/api/v1/appointments/by-dentist/**").hasAnyRole("ADMIN", "DENTIST")
                        .requestMatchers("/adsweb/api/v1/appointments").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers("/adsweb/api/v1/appointments/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers("/adsweb/api/v1/appointments/**").hasRole("ADMIN")

                        // Patients
                        .requestMatchers("/adsweb/api/v1/patients/**").hasAnyRole("ADMIN", "MEMBER")

                        // Addresses, surgeries, dentists
                        .requestMatchers("/adsweb/api/v1/addresses/**").hasRole("ADMIN")
                        .requestMatchers("/adsweb/api/v1/surgerys/**").hasRole("ADMIN")
                        .requestMatchers("/adsweb/api/v1/dentists/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
        ;
        return http.build();
    }
}
