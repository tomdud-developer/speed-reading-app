package com.src.speedreadingapp.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserRepository;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.appuser.RoleRepository;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.registration.token.ConfirmationToken;
import com.src.speedreadingapp.registration.token.ConfirmationTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ExtendWith(MockitoExtension.class)
@Transactional
class RegistrationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    AppUserService appUserService;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserProgressRepository userProgressRepository;

    AppUser testAppUser;

    RegistrationRequest request;
    @BeforeEach
    void setUp() {
        testAppUser = new AppUser();
        testAppUser.setUsername("testRegisterUsername");
        testAppUser.setEmail("register@test.gmail.com");
        testAppUser.setFirstname("testFirstname");
        testAppUser.setLastname("testLastname");
        testAppUser.setPassword("testPassword");

        request = new RegistrationRequest();
        request.setEmail(testAppUser.getEmail());
        request.setFirstname(testAppUser.getFirstname());
        request.setLastname(testAppUser.getLastname());
        request.setPassword(testAppUser.getPassword());
        request.setUsername(testAppUser.getUsername());
    }

    @AfterEach
    void tearDown() {
        //clear database
        appUserService.getUsers().forEach(user -> {
            user.setUserProgress(null);
            user.setNumbersDisappearExerciseLog(null);
            user.getRoles().clear();
        });
        userProgressRepository.findAll().forEach(userProgress -> userProgress.setAppUser(null));
        confirmationTokenRepository.deleteAll();
        userProgressRepository.deleteAll();
        appUserRepository.deleteAll();
        roleRepository.deleteAll();


        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
    }

    @Test
    void confirmToken() throws Exception {
        String token = registrationService.register(request);
        AppUser appUser = appUserRepository.findByUsername(request.getUsername());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/registration/confirm?token=" + token))
                .andExpect(status().isOk()).andReturn();

        String isConfimred = mvcResult.getResponse().getContentAsString();
        assertEquals("confirmed", isConfimred);
    }

    @Test
    void registerCorrect() throws Exception {
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/registration")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String token = mvcResult.getResponse().getContentAsString();
        Optional<ConfirmationToken> tokenOptional = confirmationTokenRepository.findByToken(token);
        assertThat(tokenOptional).isNotEmpty();
    }

}