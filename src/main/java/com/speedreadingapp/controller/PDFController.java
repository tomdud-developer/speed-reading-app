package com.speedreadingapp.controller;


import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.PDFRequestDTO;
import com.speedreadingapp.service.PDFService;
import com.speedreadingapp.util.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v2/pdfs")
@AllArgsConstructor
@Slf4j
public class PDFController {
    private final PDFService pdfService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> uploadPdf(@RequestParam MultipartFile file, @RequestParam String name) {

        PDFRequestDTO pdfRequestDTO = PDFRequestDTO.builder()
                .name(name)
                .file(file)
                .build();

        pdfService.savePDF(pdfRequestDTO);

        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .status(ResponseStatus.SUCCESS)
                .results("PDF has been saved")
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
