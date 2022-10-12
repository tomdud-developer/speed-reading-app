package com.src.speedreadingapp.controlers;


import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/v1/speed-meter-log")
@RequiredArgsConstructor
public class SpeedMeterLogController {
    private final SpeedMeterLogService speedMeterLogService;


    protected ResponseEntity<byte[]> saveLog() {
        try {
            File file = ResourceUtils.getFile("classpath:offer_template.docx");
            byte[] contents = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
