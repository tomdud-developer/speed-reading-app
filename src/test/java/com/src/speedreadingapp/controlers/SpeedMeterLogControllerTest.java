package com.src.speedreadingapp.controlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.SpeedReadingAppApplicationTests;
import com.src.speedreadingapp.jpa.appuser.*;
import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLog;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.jpa.pdfuser.PdfUserRepository;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLogRepository;
import com.src.speedreadingapp.registration.RegistrationRequest;
import com.src.speedreadingapp.registration.RegistrationService;
import com.src.speedreadingapp.registration.token.ConfirmationTokenRepository;
import org.json.JSONArray;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
class SpeedMeterLogControllerTest {
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
    SpeedMeterLogRepository speedMeterLogRepository;

    AppUser testAppUser;
    SpeedMeterLog speedMeterLog;
    RegistrationRequest request;
    String accessToken;

    @BeforeEach
    void beforeEach() {
        speedMeterLog = new SpeedMeterLog();
        speedMeterLog.setDate(LocalDateTime.now());
        speedMeterLog.setWordsperminute(200);

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
        speedMeterLogRepository.findAll().forEach(l -> l.setAppUser(null));
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
        speedMeterLogRepository.deleteAll();
        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
        assertTrue(pdfUserRepository.findAll().isEmpty());
    }

    @Test
    void saveLog() throws Exception {
        mockMvc.perform(post("/api/v1/speed-meter-log/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(speedMeterLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.wordsperminute", is(200)));
    }

    @Test
    void getLogs() throws Exception{
        mockMvc.perform(post("/api/v1/speed-meter-log/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(speedMeterLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.wordsperminute", is(200)));

        speedMeterLog.setWordsperminute(300);
        speedMeterLog.setDate(LocalDateTime.now());
        mockMvc.perform(post("/api/v1/speed-meter-log/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(speedMeterLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.wordsperminute", is(300)));

        speedMeterLog.setWordsperminute(400);
        speedMeterLog.setDate(LocalDateTime.now());
        mockMvc.perform(post("/api/v1/speed-meter-log/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(speedMeterLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.wordsperminute", is(400)));

        mockMvc.perform(get("/api/v1/speed-meter-log/get/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(speedMeterLog)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].wordsperminute", is(200)))
                .andExpect(jsonPath("$[1].wordsperminute", is(300)))
                .andExpect(jsonPath("$[2].wordsperminute", is(400)));

        mockMvc.perform(get("/api/v1/speed-meter-log/get-latest/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(speedMeterLog)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.wordsperminute", is(400)));
    }


}