package com.speedreadingapp.security;

import com.auth0.jwt.JWT;
import com.speedreadingapp.configuration.JWTConfigurationProperties;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class JWTTokenService implements JWTManager {

    private final JWTAlgorithmProvider jwtAlgorithmProvider;
    private final JWTConfigurationProperties jwtConfigurationProperties;

    @Override
    public boolean validate(String token) {
        return false;
    }

    @Override
    public String generateAccessToken(Authentication authentication, String issuerUrl) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authentication.getName())
                .withIssuer(issuerUrl)
                .withClaim("username", authentication.getName())
                .withClaim("roles", retrieveAuthorities(authentication))
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());
    }

    @Override
    public String generateRefreshToken(Authentication authentication, String issuerUrl) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 30L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authentication.getName())
                .withIssuer(issuerUrl)
                .sign(jwtAlgorithmProvider.getAlgorithm());
    }


    private List<String> retrieveAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
    }

}
