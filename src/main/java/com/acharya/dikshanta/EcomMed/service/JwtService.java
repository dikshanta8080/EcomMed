package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.configuration.security.UserPrincipal;
import com.acharya.dikshanta.EcomMed.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final Utils utils;

    public SecretKey generateKey() {
        byte[] bytes = Decoders.BASE64.decode(utils.getJwt().getSecret());
        return Keys.hmacShaKeyFor(bytes);
    }

    private Long getExpiration() {
        return utils.getJwt().getExpiry() * 60L * 1000;
    }

    public String getJwt(UserDetails userDetails) {
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("name", ((UserPrincipal) userDetails).getName())
                .claim("role", userPrincipal.getRole())
                .claim("username", userPrincipal.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + getExpiration()))
                .signWith(generateKey())
                .compact();

    }

    public Claims extractALlClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractALlClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isExpired(String token) {
        Date extractClaim = extractClaim(token, Claims::getExpiration);
        return extractClaim.before(new Date(System.currentTimeMillis()));
    }

    public boolean isTokenValid(UserDetails userDetails, String token) {
        String username = extractUsername(token);
        return !isExpired(token) && username.equals(userDetails.getUsername());
    }

}
