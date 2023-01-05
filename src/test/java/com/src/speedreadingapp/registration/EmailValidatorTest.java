package com.src.speedreadingapp.registration;

import com.src.speedreadingapp.SpeedReadingAppApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {
    @Autowired
    EmailValidator emailValidator;

    @Test
    void testEmail() {
        String correctEmail = "emailname@domain.com";
        assertTrue(emailValidator.test(correctEmail));

        String incorrectEmail = "email&gmail.com";
        assertFalse(emailValidator.test(incorrectEmail));

        incorrectEmail = "email@gmailcom";
        assertFalse(emailValidator.test(incorrectEmail));

        incorrectEmail = "email@.com";
        assertFalse(emailValidator.test(incorrectEmail));

        incorrectEmail = ".email@domain.com";
        assertFalse(emailValidator.test(incorrectEmail));
    }

}