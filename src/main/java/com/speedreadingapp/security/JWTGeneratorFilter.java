package com.speedreadingapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedreadingapp.configuration.JWTConfigurationProperties;
import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.ErrorDTO;
import com.speedreadingapp.dto.LoginRequestDTO;
import com.speedreadingapp.exception.ExceptionWhenDefiningAlgorithm;
import com.speedreadingapp.security.keyreader.KeyReader;
import com.speedreadingapp.security.keyreader.PemKeyRetriever;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.*;


public class JWTGeneratorFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTTokenService jwtTokenService;


    public JWTGeneratorFilter(AuthenticationManager authenticationManager, JWTTokenService jwtTokenService) {
        super(new AntPathRequestMatcher("/api/v2/login"));
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
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
                                            FilterChain chain, Authentication authenticationResult
    ) throws IOException {

        String accessToken = jwtTokenService.generateAccessToken(
                authenticationResult,
                request.getRequestURL().toString().replace("login", "token/verify")
        );

        String refreshToken = jwtTokenService.generateRefreshToken(
                authenticationResult,
                request.getRequestURL().toString().replace("login", "token/verify")
        );

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("token_type", "bearer");

        ApiResponse<Map<String, String>> responseDTO = ApiResponse
                .<Map<String, String>>builder()
                .status("SUCCESS")
                .results(tokens)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .status("UNAUTHORIZED")
                .errors(List.of(new ErrorDTO("login or password", "Invalid login or password")))
                .build();

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        response.setStatus(401);
    }
}
