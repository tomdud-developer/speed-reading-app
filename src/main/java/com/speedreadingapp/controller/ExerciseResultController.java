package com.speedreadingapp.controller;


import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.DisappearNumbersResultRequestDTO;
import com.speedreadingapp.dto.DisappearNumbersResultResponseDTO;
import com.speedreadingapp.service.ExerciseResultService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/exercises-results")
@AllArgsConstructor
@Slf4j
public class ExerciseResultController {

    private final ExerciseResultService exerciseResultService;

    @PostMapping("/disappear-numbers")
    public ResponseEntity<ApiResponse<DisappearNumbersResultResponseDTO>> saveDisappearNumbersResult(
            @RequestBody @Valid DisappearNumbersResultRequestDTO disappearNumbersResultRequestDTO) {

        DisappearNumbersResultResponseDTO disappearNumbersResultResponseDTO =
                exerciseResultService.saveDisappearNumbersResult(disappearNumbersResultRequestDTO);

        ApiResponse<DisappearNumbersResultResponseDTO> apiResponse = ApiResponse
                .<DisappearNumbersResultResponseDTO>builder()
                .status("Success")
                .results(disappearNumbersResultResponseDTO)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/disappear-numbers")
    public ResponseEntity<ApiResponse<List<DisappearNumbersResultResponseDTO>>> getAllDisappearNumbersResults() {

        List<DisappearNumbersResultResponseDTO> disappearNumbersResultResponseDTOList =
                exerciseResultService.getDisappearNumbersResults();

        ApiResponse<List<DisappearNumbersResultResponseDTO>> apiResponse = ApiResponse
                .<List<DisappearNumbersResultResponseDTO>>builder()
                .status("Success")
                .results(disappearNumbersResultResponseDTOList)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
