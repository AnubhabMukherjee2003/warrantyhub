package com.anubhab.warrantyhub.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

        
        private static final String SECRET = "WarrantyHubSecretKeyForJwtAuthentication123456789";

        private final SecretKey secretKey = Keys.hmacShaKeyFor(
                        SECRET.getBytes(StandardCharsets.UTF_8));

        public String generateToken(UserDetails userDetails) {

                return Jwts.builder()
                                .subject(userDetails.getUsername())
                                .issuedAt(new Date())
                                .expiration(
                                                new Date(
                                                                System.currentTimeMillis()
                                                                                + 1000 * 60 * 60))
                                .signWith(secretKey)
                                .compact();
        }

        public String extractUsername(String token) {

                return Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .getSubject();
        }

        public boolean isTokenValid(
                        String token,
                        UserDetails userDetails) {

                String username = extractUsername(token);

                return username.equals(userDetails.getUsername())
                                && !isTokenExpired(token);
        }

        private boolean isTokenExpired(String token) {

                Date expiration = Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .getExpiration();

                return expiration.before(new Date());
        }
}