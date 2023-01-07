package com.src.speedreadingapp.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.SpeedReadingAppApplicationTests;
import com.src.speedreadingapp.jpa.appuser.*;
import com.src.speedreadingapp.jpa.course.UserProgress;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.jpa.course.UserProgressService;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLog;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLogRepostitory;
import com.src.speedreadingapp.jpa.understandingmeter.textquestions.UnderstandingLevelQuestionRepository;
import com.src.speedreadingapp.registration.RegistrationRequest;
import com.src.speedreadingapp.registration.RegistrationService;
import com.src.speedreadingapp.registration.token.ConfirmationTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ExtendWith(MockitoExtension.class)
class UserProgressControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserProgressRepository userProgressRepository;
    @Autowired
    UserProgressService userProgressService;

    AppUser testAppUser;
    RegistrationRequest request;
    String accessToken;
    UserProgress userProgress;

    @BeforeEach
    void beforeEach() {
        testAppUser = new AppUser();
        testAppUser.setUsername("testUsername2");
        testAppUser.setEmail("test2@test.gmail.com");
        testAppUser.setFirstname("testFirstname");
        testAppUser.setLastname("testLastname");
        testAppUser.setPassword("testPassword");
        testAppUser.setRoles(new HashSet<>());


        request = new RegistrationRequest();
        request.setEmail(testAppUser.getEmail());
        request.setFirstname(testAppUser.getFirstname());
        request.setLastname(testAppUser.getLastname());
        request.setPassword(testAppUser.getPassword());
        request.setUsername(testAppUser.getUsername());


        roleRepository.save(new Role(1L,"USER_ROLE"));
        String token = registrationService.register(request);
        registrationService.confirmToken(token);
        testAppUser = appUserService.getAppUser(testAppUser.getUsername());
        assertTrue(testAppUser.isEnabled());

        assertNotNull(testAppUser.getUserProgress());
        assertEquals(1, testAppUser.getUserProgress().getCurrentSessionNumber());
        try {
            accessToken = SpeedReadingAppApplicationTests.login(mockMvc, testAppUser.getUsername(), "testPassword");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        appUserService.getUsers().forEach(user -> {
            user.setUserProgress(null);
            user.setNumbersDisappearExerciseLog(null);
            user.getRoles().clear();
            user.setSpeedMeterLogs(null);
        });

        userProgressRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
        appUserRepository.deleteAll();
        roleRepository.deleteAll();
        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
    }

    @Test
    void getUserProgress() throws Exception{
        mockMvc.perform(get("/api/v1/user-progress/get/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(5)))
                .andExpect(jsonPath("$.currentSessionNumber", is(1)))
                .andExpect(jsonPath("$.finishedExercise", is(0)));

        mockMvc.perform(post("/api/v1/user-progress/confirm-exercise/" + testAppUser.getId() + "&2")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/user-progress/get/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(5)))
                .andExpect(jsonPath("$.currentSessionNumber", is(1)))
                .andExpect(jsonPath("$.finishedExercise", is(2)));

        mockMvc.perform(post("/api/v1/user-progress/confirm-session/" + testAppUser.getId() + "&5")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().is5xxServerError());

        mockMvc.perform(post("/api/v1/user-progress/confirm-session/" + testAppUser.getId() + "&1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/user-progress/get/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(5)))
                .andExpect(jsonPath("$.currentSessionNumber", is(2)))
                .andExpect(jsonPath("$.finishedExercise", is(0)));
    }


    @Test
    void confirmSession() throws Exception {
    }

    @Test
    void resetProgress() throws Exception {
    }
}