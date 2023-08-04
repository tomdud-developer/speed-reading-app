package com.speedreadingapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PDFServiceTest {

    @Autowired
    private PDFService pdfService;

    @Test
    void test() throws IOException {

        URL urlToPDF = PDFServiceTest.class.getClassLoader().getResource("Lorem_ipsum.pdf");
        File file = new File(urlToPDF.getPath());
        Assertions.assertTrue(file.exists());


        pdfService.generateHTMLFromPDF(file);
    }

}