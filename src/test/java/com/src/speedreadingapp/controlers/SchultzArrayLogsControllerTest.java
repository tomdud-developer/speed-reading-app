package com.src.speedreadingapp.controlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.SpeedReadingAppApplicationTests;
import com.src.speedreadingapp.jpa.appuser.*;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.jpa.pdfuser.PdfUserRepository;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayLog;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayLogRepository;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLogRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
class SchultzArrayLogsControllerTest {


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
    PdfUserRepository pdfUserRepository;
    @Autowired
    SchultzArrayLogRepository schultzArrayLogRepository;

    AppUser testAppUser;
    SchultzArrayLog schultzArrayLog;
    RegistrationRequest request;
    String accessToken;

    @BeforeEach
    void beforeEach() {
        schultzArrayLog = new SchultzArrayLog();
        schultzArrayLog.setLog2x2(5);

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
        pdfUserRepository.deleteAll();
        schultzArrayLogRepository.deleteAll();
        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
        assertTrue(pdfUserRepository.findAll().isEmpty());
    }

    @Test
    void putLogs() throws Exception {
        mockMvc.perform(put("/api/v1/schultz-array-logs/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schultzArrayLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(26)))
                .andExpect(jsonPath("$.log2x2", is(5)));
    }

    @Test
    void getLogs() throws Exception{
        schultzArrayLog.setLog2x4(100);
        mockMvc.perform(put("/api/v1/schultz-array-logs/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schultzArrayLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(26)))
                .andExpect(jsonPath("$.log2x4", is(100)));

        schultzArrayLog.setLog2x6(9);
        mockMvc.perform(put("/api/v1/schultz-array-logs/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schultzArrayLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(26)))
                .andExpect(jsonPath("$.log2x6", is(9)));

        //Get perform
        mockMvc.perform(get("/api/v1/schultz-array-logs/get/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(26)))
                .andExpect(jsonPath("$.log2x6", is(9)))
                .andExpect(jsonPath("$.log2x4", is(100)))
                .andExpect(jsonPath("$.log2x2", is(5)));
    }
}