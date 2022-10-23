package com.src.speedreadingapp.jpa.pdfuser;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Repository
public interface PdfUserRepository extends JpaRepository<PdfUser, Long> {



}
