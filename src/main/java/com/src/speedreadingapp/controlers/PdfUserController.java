package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.pdfuser.PdfUser;
import com.src.speedreadingapp.jpa.pdfuser.PdfUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/pdfuser")
@RequiredArgsConstructor
public class PdfUserController {

    private final PdfUserService pdfUserService;

    @PostMapping(value = "/save/{userId}&{pageFrom}&{pageTo}")
    Long uploadPdf(@RequestParam MultipartFile multipartPdf
                        , @PathVariable("userId") Long userId
                            , @PathVariable("pageFrom") Integer pageFrom
                                , @PathVariable("pageTo") Integer pageTo) throws Exception {
        return pdfUserService.save(multipartPdf, userId, pageFrom , pageTo);
    }

    @GetMapping(value = "/get/{userId}")
    PdfUser download(@PathVariable("userId") Long userId) {
        return pdfUserService.download(userId);
    }

    @GetMapping(value = "/get-text/{userId}&{nWords}")
    String getText(@PathVariable("userId") Long userId, @PathVariable("nWords") Integer nWords) {
        return pdfUserService.getNwords(userId, nWords);
    }

}
