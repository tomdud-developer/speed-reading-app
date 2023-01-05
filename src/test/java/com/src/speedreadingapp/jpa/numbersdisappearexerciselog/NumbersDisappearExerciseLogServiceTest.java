package com.src.speedreadingapp.jpa.numbersdisappearexerciselog;

import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserRepository;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.appuser.RoleRepository;
import com.src.speedreadingapp.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class NumbersDisappearExerciseLogServiceTest {

    @Autowired
    NumbersDisappearExerciseLogService numbersDisappearExerciseLogService;

    @Autowired
    AppUserService appUserService;
/*    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    ConfirmationTokenService confirmationTokenService;
    @MockBean
    RoleRepository roleRepository;*/

   /* @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserService appUserService;*/

    AppUser testAppUser;

    @BeforeEach
    void beforeEach() {
        //Setting up properties of testUser
        testAppUser = new AppUser();
        testAppUser.setUsername("testUsername");
        testAppUser.setEmail("test@test.gmail.com");
        testAppUser.setFirstname("testFirstname");
        testAppUser.setLastname("testLastname");
        testAppUser.setPassword("testPassword");

        //SignUpTestUser
        //appUserService.signUpUser(testAppUser);
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {


        //AppUser gettingAppUser = appUserService.getAppUser(testAppUser.getUsername());
        //numbersDisappearExerciseLogService.save(new NumbersDisappearExerciseLog(1L, 2, 2,2,2,2,2,2,2,2,2,2,null), 1);
    //    String str = appUserService.signUpUser(testAppUser);
       // System.out.println(appUserRepository.findAll());
        System.out.println("In1");
        System.out.println("In2");
    }

    @Test
    void get() {
    }



}