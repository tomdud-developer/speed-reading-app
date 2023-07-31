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

    private final JWTConfigurationProperties jwtConfigurationProperties;
    private final AuthenticationManager authenticationManager;
    private final JWTAlgorithmProvider jwtAlgorithmProvider;
    public JWTGeneratorFilter(AuthenticationManager authenticationManager, JWTAlgorithmProvider jwtAlgorithmProvider, JWTConfigurationProperties jwtConfigurationProperties) {
        super(new AntPathRequestMatcher("/api/v2/login"));
        this.authenticationManager = authenticationManager;
        this.jwtAlgorithmProvider = jwtAlgorithmProvider;
        this.jwtConfigurationProperties = jwtConfigurationProperties;
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
                                            FilterChain chain, Authentication authenticationResult)
            throws IOException {

        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authenticationResult.getName())
                .withClaim("username", authenticationResult.getName())
                .withClaim("roles", retrieveAuthorities(authenticationResult))
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());

        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 30L * 60 * jwtConfigurationProperties.getAccessTokenExpirationInMinutes()))
                .withSubject(authenticationResult.getName())
                .withIssuer(request.getRequestURL().toString())
                .sign(jwtAlgorithmProvider.getAlgorithm());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("token_type", "bearer");
        tokens.put("expires_in", jwtConfigurationProperties.getAccessTokenExpirationInMinutes() * 60 + "");

        ApiResponse<Map<String, String>> responseDTO = ApiResponse
                .<Map<String, String>>builder()
                .status("SUCCESS")
                .results(tokens)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), responseDTO);
    }

    private List<String> retrieveAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
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
