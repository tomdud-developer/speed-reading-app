package com.src.speedreadingapp.jpa.pdfuser;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class PdfUserService {
    final private AppUserService appUserService;
    final private PdfUserRepository pdfUserRepository;

    @Transactional
    public Long save(MultipartFile multipartPdf,
                     Long userId,
                     Integer pageFrom,
                     Integer pageTo) throws UsernameNotFoundException, IOException {
        PdfUser pdfUser = new PdfUser();
        pdfUser.setName(multipartPdf.getOriginalFilename());
        pdfUser.setContent(PdfUser.pdfToString(multipartPdf.getBytes(), pageFrom, pageTo));
        PdfUser savedPdf = pdfUserRepository.save(pdfUser);
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        optionalAppUser.ifPresentOrElse((value) -> {
            value.setPdfUser(savedPdf);
        }, () -> {
            throw new UsernameNotFoundException("This user doesn't exsist in database!");
        });
        return savedPdf.getId();
    }

    public String getNwords(Long userId, Integer nWords) {
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isPresent()) {
            return optionalAppUser.get().getPdfUser().getNwords(nWords);
        }
        else
            throw new RuntimeException("This user doesn't exsist in database!");
    }

    public PdfUser download(Long userId) {
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isPresent()) {
            return optionalAppUser.get().getPdfUser();
        }
        else
            throw new RuntimeException("This user doesn't exsist in database!");
    }
}
