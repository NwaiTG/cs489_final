package com.nwai.dentalsys.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;


import java.util.Date;
import java.util.Collection;

import static java.security.KeyRep.Type.SECRET;

@Service
public class JWTService {
    @Value("${jwt.secretKey}") //get from application properties
    private String SERCRET;

    // authorithies should contains role
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .signWith(signInKey()) //should be sign with integrity
                .issuedAt(new Date())
                .issuer("miu.edu")
                .expiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24))
                .subject(userDetails.getUsername())
                .claim("authorities", populateAuthorities(userDetails.getAuthorities())) //get authorities, read,write
                .compact();
    }

    private javax.crypto.SecretKey signInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SERCRET));
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(","));
    }

    //return claims 2nd path of JWT Token
    public Claims parseSignedCalims(String token) {
        return Jwts.parser()
                .verifyWith(signInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
