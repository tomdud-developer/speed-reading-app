package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.jpa.file.File;
import com.src.speedreadingapp.jpa.file.FileService;
import com.src.speedreadingapp.jpa.pdfuser.PdfUser;
import com.src.speedreadingapp.jpa.pdfuser.PdfUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;


@RestController
@RequestMapping("api/v1/esp-storage/")
@RequiredArgsConstructor
public class ESPController {

    private final FileService fileService;

    @PostMapping(value = "/save")
    Long save(@RequestParam MultipartFile multipartFile) throws Exception {
        return fileService.save(multipartFile);
    }

    /*
    @GetMapping(value = "/get/{id}")
    MultipartFile save(@PathVariable("id") Long id) throws Exception {
        File f = fileService.get(id);
        java.io.File file = new java.io.File(f.getName());
        BufferedWriter writer = new BufferedWriter(file);
        file.

        return fileService.get(id).getBytes();
    }

    */

}
