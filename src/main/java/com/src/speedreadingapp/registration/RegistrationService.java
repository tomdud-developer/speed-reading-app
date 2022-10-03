package com.src.speedreadingapp.registration;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.registration.emailsender.EmailBuilder;
import com.src.speedreadingapp.registration.emailsender.EmailSender;
import com.src.speedreadingapp.registration.token.ConfirmationToken;
import com.src.speedreadingapp.registration.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
public class RegistrationService {


    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    @Autowired
    public RegistrationService(EmailValidator emailValidator, AppUserService appUserService, ConfirmationTokenService confirmationTokenService, EmailSender emailSender) {
        this.emailValidator = emailValidator;
        this.appUserService = appUserService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
    }

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail) {
            throw new IllegalStateException("Email not valid");
        }

        String token = appUserService.signUpUser(
                new AppUser(null,
                        request.getFirstname(),
                        request.getLastname(),
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword(),
                        false,
                        false,
                        new HashSet<>()
                )
        );

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), EmailBuilder.buildEmail(request.getFirstname(), link));

        return token;
    }


    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getComfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    public int enableAppUser(String email) {
        return appUserService.enableAppUser(email);
    }

}
