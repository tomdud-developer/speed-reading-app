package com.speedreadingapp.controller;


import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.PDFNamesResponseDTO;
import com.speedreadingapp.dto.PDFPagesRequestDTO;
import com.speedreadingapp.dto.PDFRequestDTO;
import com.speedreadingapp.service.PDFService;
import com.speedreadingapp.service.pdf.HTMLPageFromPDF;
import com.speedreadingapp.util.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v2/pdfs")
@AllArgsConstructor
@Slf4j
@Tag(name = "PDF Controller")
public class PDFController {
    private final PDFService pdfService;

    @Operation(
            description = "Upload PDF to server resources to use it in exercises",
            summary = "POST PDF"
    )
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

    @GetMapping ("/list")
    public ResponseEntity<ApiResponse<List<PDFNamesResponseDTO>>> getListOfPDFsNames() {

        List<PDFNamesResponseDTO> listOfPDFsNames = pdfService.getListOfPDFsNames();

        ApiResponse<List<PDFNamesResponseDTO>> apiResponse = ApiResponse
                .<List<PDFNamesResponseDTO>>builder()
                .status(ResponseStatus.SUCCESS)
                .results(listOfPDFsNames)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping ("/pages")
    public ResponseEntity<ApiResponse<List<HTMLPageFromPDF>>> getListOfHTMLPages(@RequestBody PDFPagesRequestDTO pdfPagesRequestDTO) {

        List<HTMLPageFromPDF> listOfPages = pdfService.getListOfPagesInHTML(pdfPagesRequestDTO);

        ApiResponse<List<HTMLPageFromPDF>> apiResponse = ApiResponse
                .<List<HTMLPageFromPDF>>builder()
                .status(ResponseStatus.SUCCESS)
                .results(listOfPages)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
