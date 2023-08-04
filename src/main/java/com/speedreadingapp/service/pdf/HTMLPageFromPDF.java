package com.speedreadingapp.service.pdf;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HTMLPageFromPDF {

    private String htmlPageContent;
    private int pageNumberOfPDF;
    private int wordsOnPage;

}
