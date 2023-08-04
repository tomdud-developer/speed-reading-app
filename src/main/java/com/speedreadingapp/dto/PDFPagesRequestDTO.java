package com.speedreadingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PDFPagesRequestDTO {

    private String pdfName;
    private int desiredWords;
    private int fromPage;

}
