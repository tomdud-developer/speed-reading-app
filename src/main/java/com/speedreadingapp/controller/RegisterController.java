package com.speedreadingapp.controller;

import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.service.ApplicationUserService;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/v2")
@AllArgsConstructor
@Slf4j
public class RegisterController {

    private final ApplicationUserService applicationUserService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {

        log.info("LoginAndRegisterController::register request body {}",
                ObjectToJsonAsStringConverter.convert(registerRequestDTO));

        String result = applicationUserService.register(registerRequestDTO);

        ApiResponse<String> responseDTO = ApiResponse
                .<String>builder()
                .status("Success")
                .results(result)
                .build();

        log.info("LoginAndRegisterController::register response {}", ObjectToJsonAsStringConverter.convert(responseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

}

