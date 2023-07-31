package com.speedreadingapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.ErrorDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.exception.AuthoritiesNotMatchWithTokenClaimsException;
import com.speedreadingapp.repository.ApplicationUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {

    private final JWTTokenService jwtTokenService;

    public JWTValidationFilter(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (isContainAuthorizationHeaderWithBearerKeyword(authorizationHeader)) {
            String token = retrieveTokenFromHeader(authorizationHeader);
            try {
                jwtTokenService.validate(token);
                filterChain.doFilter(request, response);
            } catch (JWTVerificationException | UsernameNotFoundException | AuthoritiesNotMatchWithTokenClaimsException exception) {
                ApiResponse<String> apiResponse = ApiResponse
                        .<String>builder()
                        .status("UNAUTHORIZED")
                        .errors(List.of(new ErrorDTO("access_token", exception.getMessage())))
                        .build();

                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else filterChain.doFilter(request, response);
    }

    private boolean isContainAuthorizationHeaderWithBearerKeyword(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String retrieveTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/v2/login")
                || request.getServletPath().equals("/api/v2/token/refresh")
                || request.getServletPath().equals("/api/v2/register")
                ;
    }
}
