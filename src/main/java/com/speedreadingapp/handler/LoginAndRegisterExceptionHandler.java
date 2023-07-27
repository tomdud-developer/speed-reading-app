package com.speedreadingapp.handler;

import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.ErrorDTO;
import com.speedreadingapp.exception.UserAlreadyRegisteredException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

public class LoginAndRegisterExceptionHandler {


    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ApiResponse<?> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException exception) {
        ApiResponse<?> serviceResponse = new ApiResponse<>();
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        return serviceResponse;
    }
}
