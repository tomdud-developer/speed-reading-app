package com.speedreadingapp.controller;

import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.RefreshTokenResponseDTO;
import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.security.token.JWTTokenService;
import com.speedreadingapp.security.token.JWTTokenUtils;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/token")
@AllArgsConstructor
@Slf4j
public class TokenController {

    private final JWTTokenService jwtTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {

        log.info("TokenController::refresh token from Authorization header is {}", refreshToken);

        String accessToken = jwtTokenService.generateAccessTokenBasedOnRefreshToken(
                JWTTokenUtils.retrieveTokenFromHeader(refreshToken));

        ApiResponse<RefreshTokenResponseDTO> responseDTO = ApiResponse
                .<RefreshTokenResponseDTO>builder()
                .status("Success")
                .results(new RefreshTokenResponseDTO(accessToken))
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verify(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        log.info("TokenController::verify token from Authorization header is {}", token);

        jwtTokenService.validate(JWTTokenUtils.retrieveTokenFromHeader(token));

        ApiResponse<String> responseDTO = ApiResponse
                .<String>builder()
                .status("Success")
                .results("Token is valid")
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
