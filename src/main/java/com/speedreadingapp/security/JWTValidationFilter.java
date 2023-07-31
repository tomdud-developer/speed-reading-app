package com.speedreadingapp.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.ErrorDTO;
import com.speedreadingapp.exception.AuthoritiesNotMatchWithTokenClaimsException;
import com.speedreadingapp.security.token.JWTTokenService;
import com.speedreadingapp.security.token.JWTTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

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
        if (!shouldNotFilter(request) && JWTTokenUtils.isContainAuthorizationHeaderWithBearerKeyword(authorizationHeader)) {
            String token = JWTTokenUtils.retrieveTokenFromHeader(authorizationHeader);
            try {
                jwtTokenService.validateAccessToken(token);
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/v2/login")
                || request.getRequestURI().equals("/api/v2/token/refresh")
                || request.getRequestURI().equals("/api/v2/register")
                || request.getRequestURI().equals("/api/v2/token/verify")
                ;
    }
}
