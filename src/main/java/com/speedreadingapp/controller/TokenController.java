package com.speedreadingapp.controller;

import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/token")
@AllArgsConstructor
@Slf4j
public class TokenController {

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {

        log.info("TokenController::refresh token from Authorization header is {}", refreshToken);


        ApiResponse<String> responseDTO = ApiResponse
                .<String>builder()
                .status("Success")
                .results(null)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
