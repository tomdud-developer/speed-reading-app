package com.src.speedreadingapp.jpa.file;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.pdfuser.PdfUser;
import com.src.speedreadingapp.jpa.pdfuser.PdfUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    final private FileRepository fileRepository;

    @Transactional
    public Long save(MultipartFile multipartFile) throws Exception {
        File newFile = new File();
        try {
            newFile.setName(multipartFile.getOriginalFilename());
            newFile.setBytes(multipartFile.getBytes());
            File saved = fileRepository.save(newFile);
            return saved.getId();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public File get(Long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if(optionalFile.isPresent())
            return optionalFile.get();
        else
            throw new RuntimeException("Brak pliku o takim id");
    }
}