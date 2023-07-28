package com.speedreadingapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedreadingapp.dto.LoginRequestDTO;
import com.speedreadingapp.security.keyreader.KeyReader;
import com.speedreadingapp.security.keyreader.PemKeyRetriever;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.*;


public class JWTGeneratorFilter extends AbstractAuthenticationProcessingFilter {

    //@Value("${jwt.sha.publicKey}")
    private String jwtShaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFMTTepzE1j3uIa5o4yfbvztX8\n" +
            "F3cmUk8U8DXh2D0PnRHwieO0tNFWw9ER5LDW7s9zf6W7/nwf+C8TEd0C7Myte1cD\n" +
            "Gzs2CCKrRkkPNDNE4MKpjp/hkHbAM7Z1ACthhbKwaJMZUjvk4poG+PLQPgi05QkG\n" +
            "viB+YxzWKgQggsQPuQIDAQAB";

    //@Value("${jwt.sha.secretKey}")
    private String jwtShaSecretKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMUxNN6nMTWPe4hr\n" +
            "mjjJ9u/O1fwXdyZSTxTwNeHYPQ+dEfCJ47S00VbD0RHksNbuz3N/pbv+fB/4LxMR\n" +
            "3QLszK17VwMbOzYIIqtGSQ80M0TgwqmOn+GQdsAztnUAK2GFsrBokxlSO+Timgb4\n" +
            "8tA+CLTlCQa+IH5jHNYqBCCCxA+5AgMBAAECgYAoo0VkBUys3w70REiaH3UWYqfS\n" +
            "tftGGHxXslFIY8nhp9sEmUYl/YWOvfgeGeUfxkNWaB39qipmZD0/TtUfPzHGyexB\n" +
            "c5kp5m45j+sbYIwP+ohAYE2UEQ5Z1viDfOnFhzwlYK7NmX2Xz4TCEQ5V+5u1nAbx\n" +
            "0goClmoWaFp6t6SwyQJBAPB9E9mL/8DOqsBAHtYIvdvYLMbqkC2TkAMV44QDr4sp\n" +
            "fql6AksbWQ984XA0MIDV8f6FVEjXT8QO5/qpXvJEXrsCQQDR6TlLt/qA8pNrJQr8\n" +
            "j5VDAbVeK5ZUs5hqNFRFzrbZj6pIGRWoVPMkzBEkgWGRuO+2xsLaqG+NL7rPUGL6\n" +
            "nhYbAkASgk3ozHGesUlLCqRU7M9QAE9R7/Owzk6jLigYnQABwevRt2Y9yZkNLBtd\n" +
            "u2aQQ+cgI7rc8FVfTZZlIGwCUWjJAkEAu9iSRYhmzG5ILmH/6vQzBrvIqnUnGrV9\n" +
            "d81MfQv35coDAHIyR2l+DTfxP1HpFpcBLffA+Bwzd413B39QlCZUcQJBANETTrGy\n" +
            "Ju37Dpe/9ohuDgQLkAGTEkEQpLJy4QMlpvHqSX5A0Csa0VQ4n6FN8jIUc+zCkJ8N\n" +
            "+HL4Egh52z61kVQ=";

    @Value("${jwt.expirationInMinutes}")
    private long jwtExpirationInMinutes = 120;

    private final AuthenticationManager authenticationManager;

    public JWTGeneratorFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/v1/login"));
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        InputStream inputStream = request.getInputStream();

        LoginRequestDTO loginRequestDTO = new ObjectMapper().readValue(inputStream, LoginRequestDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authenticationResult)
            throws IOException {

        String jwt = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * jwtExpirationInMinutes))
                .withSubject(authenticationResult.getName())
                .withClaim("username", authenticationResult.getName())
                .withClaim("authorities", retrieveAuthorities(authenticationResult))
                .withClaim("generation-datetime", Instant.now())
                .sign(getAlgorithm());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwt);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    private Algorithm getAlgorithm() {
        try {
            PemKeyRetriever pemKeyRetriever = new KeyReader("RSA", jwtShaSecretKey, jwtShaPublicKey);

            RSAPublicKey rsaPublicKey = (RSAPublicKey) pemKeyRetriever.getPemPublicKey();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) pemKeyRetriever.getPemPrivateKey();

            return  Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }






    private List<String> retrieveAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
    }
}
