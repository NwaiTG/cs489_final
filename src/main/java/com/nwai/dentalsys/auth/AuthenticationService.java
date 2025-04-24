package com.nwai.dentalsys.auth;

import com.nwai.dentalsys.config.JWTService;
import com.nwai.dentalsys.user.User;
import com.nwai.dentalsys.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    //For login
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  authenticationRequest.username(),
                  authenticationRequest.password()
          )
        );

        //if user exists need to generate token.
        var user = (User) authentication.getPrincipal(); // get all user information.
        String token = "";
        if(user != null){
            token = jwtService.generateToken(user);
        }

        return new AuthenticationResponse(token);
    }

    //For register
    public AuthenticationResponse register(RegisterRequestDto registerRequestDto){
        //create user object
        User user = new User(
                registerRequestDto.firstName(),
                registerRequestDto.lastName(),
                registerRequestDto.username(),
                passwordEncoder.encode(registerRequestDto.password()),
                registerRequestDto.role()
        );

        //save in db
        User registerUser = userRepository.save(user);

        //generate token
        String token = jwtService.generateToken(registerUser);
        return new AuthenticationResponse(token);
    }
}
