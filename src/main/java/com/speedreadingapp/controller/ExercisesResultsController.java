package com.speedreadingapp.controller;


import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.exercise.DisappearNumbersResultRequestDTO;
import com.speedreadingapp.dto.exercise.DisappearNumbersResultResponseDTO;
import com.speedreadingapp.dto.exercise.SpeedMeterResultRequestDTO;
import com.speedreadingapp.dto.exercise.SpeedMeterResultResponseDTO;
import com.speedreadingapp.service.ExerciseResultService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.speedreadingapp.util.ResponseStatus;

import java.util.List;

@RestController
@RequestMapping("api/v2/exercises-results")
@AllArgsConstructor
@Slf4j
public class ExercisesResultsController {

    private final ExerciseResultService exerciseResultService;

    @PostMapping("/disappear-numbers")
    public ResponseEntity<ApiResponse<DisappearNumbersResultResponseDTO>> saveDisappearNumbersResult(
            @RequestBody @Valid DisappearNumbersResultRequestDTO disappearNumbersResultRequestDTO) {

        DisappearNumbersResultResponseDTO disappearNumbersResultResponseDTO =
                exerciseResultService.saveDisappearNumbersResult(disappearNumbersResultRequestDTO);

        ApiResponse<DisappearNumbersResultResponseDTO> apiResponse = ApiResponse
                .<DisappearNumbersResultResponseDTO>builder()
                .status(ResponseStatus.SUCCESS)
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
                .status(ResponseStatus.SUCCESS)
                .results(disappearNumbersResultResponseDTOList)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/speed-meter")
    public ResponseEntity<ApiResponse<SpeedMeterResultResponseDTO>> saveDisappearNumbersResult(
            @RequestBody @Valid SpeedMeterResultRequestDTO speedMeterResultRequestDTO) {

        SpeedMeterResultResponseDTO speedMeterResultResponseDTO =
                exerciseResultService.saveSpeedMeterResult(speedMeterResultRequestDTO);

        ApiResponse<SpeedMeterResultResponseDTO> apiResponse = ApiResponse
                .<SpeedMeterResultResponseDTO>builder()
                .status(ResponseStatus.SUCCESS)
                .results(speedMeterResultResponseDTO)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/speed-meter")
    public ResponseEntity<ApiResponse<List<SpeedMeterResultResponseDTO>>> getAllSpeedMeterResults() {

        List<SpeedMeterResultResponseDTO> speedMeterResultResponseDTOList =
                exerciseResultService.getSpeedMeterResults();

        ApiResponse<List<SpeedMeterResultResponseDTO>> apiResponse = ApiResponse
                .<List<SpeedMeterResultResponseDTO>>builder()
                .status(ResponseStatus.SUCCESS)
                .results(speedMeterResultResponseDTOList)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
