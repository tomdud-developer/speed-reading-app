package com.speedreadingapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.ErrorDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {


    private final JWTAlgorithmProvider jwtAlgorithmProvider;

    public JWTValidationFilter(JWTAlgorithmProvider jwtAlgorithmProvider) {
        this.jwtAlgorithmProvider = jwtAlgorithmProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (isContainAuthorizationHeaderWithBearerKeyword(authorizationHeader)) {
            String token = retrieveTokenFromHeader(authorizationHeader);
            try {
                proceedAuthorizationJWTToken(token);
                filterChain.doFilter(request, response);
            } catch (JWTVerificationException exception) {
                ApiResponse<String> apiResponse = new ApiResponse<>();
                List<ErrorDTO> errors = new ArrayList<>();
                errors.add(new ErrorDTO("Authorization JWT token", exception.getMessage()));
                apiResponse.setErrors(errors);
                apiResponse.setStatus("Forbidden");

                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else filterChain.doFilter(request, response);
    }

    private boolean isContainAuthorizationHeaderWithBearerKeyword(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String retrieveTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    private void proceedAuthorizationJWTToken(String token) {
        JWTVerifier verifier = JWT.require(jwtAlgorithmProvider.getAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/v1/login") || request.getServletPath().equals("/api/v1/token/refresh");
    }
}
