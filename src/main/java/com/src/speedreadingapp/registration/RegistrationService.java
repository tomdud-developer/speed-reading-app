package com.src.speedreadingapp.registration;

import com.src.speedreadingapp.jpa.course.UserProgress;
import com.src.speedreadingapp.jpa.course.UserProgressService;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.registration.emailsender.EmailBuilder;
import com.src.speedreadingapp.registration.emailsender.EmailSender;
import com.src.speedreadingapp.registration.token.ConfirmationToken;
import com.src.speedreadingapp.registration.token.ConfirmationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@Slf4j
public class RegistrationService {


    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final UserProgressService userProgressService;

    @Autowired
    public RegistrationService(EmailValidator emailValidator, AppUserService appUserService, ConfirmationTokenService confirmationTokenService, EmailSender emailSender, UserProgressService userProgressService) {
        this.emailValidator = emailValidator;
        this.appUserService = appUserService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
        this.userProgressService = userProgressService;
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
                        new HashSet<>(),
                        new HashSet<>(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
        String link = "http://speedreadingapplicationbackend.eu.loclx.io/" +
                        "api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), EmailBuilder.buildEmail(request.getFirstname(), link));
        return token;
    }


    @Transactional
    public String confirmToken(String token) throws IllegalStateException{
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

        AppUser appUser = confirmationToken.getAppUser();
        confirmationTokenService.setConfirmedAt(token);
        //appUserService.enableAppUser(appUser.getEmail());
        appUser.setEnabled(true);
        log.info("Create progress Entity for user");
        UserProgress userProgress = userProgressService.createNewAndSaveUserProgress(appUser);
        appUser.setUserProgress(userProgress);

        log.info("Provide primary perviligies for user");
        appUserService.addRoleToUser(confirmationToken.getAppUser().getUsername(), "ROLE_USER");
        return "confirmed";
    }


}
