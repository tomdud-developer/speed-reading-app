package com.speedreadingapp.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.ErrorDTO;
import com.speedreadingapp.exception.PDFServiceException;
import com.speedreadingapp.util.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class PDFExceptionHandler {

    @ExceptionHandler({PDFServiceException.class})
    public ResponseEntity<ApiResponse<String>> handlePDFServiceException(Exception exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(ResponseStatus.ERROR);
        apiResponse.setErrors(Collections.singletonList(new ErrorDTO("name or file", exception.getMessage())));

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
