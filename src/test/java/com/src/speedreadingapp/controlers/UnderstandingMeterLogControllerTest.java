package com.src.speedreadingapp.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.SpeedReadingAppApplicationTests;
import com.src.speedreadingapp.jpa.appuser.*;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.jpa.pdfuser.PdfUserRepository;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLogRepository;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLog;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLogRepostitory;
import com.src.speedreadingapp.jpa.understandingmeter.textquestions.UnderstandingLevelQuestion;
import com.src.speedreadingapp.jpa.understandingmeter.textquestions.UnderstandingLevelQuestionRepository;
import com.src.speedreadingapp.registration.RegistrationRequest;
import com.src.speedreadingapp.registration.RegistrationService;
import com.src.speedreadingapp.registration.token.ConfirmationTokenRepository;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ExtendWith(MockitoExtension.class)
class UnderstandingMeterLogControllerTest {

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
    UnderstandingLevelLogRepostitory understandingLevelLogRepostitory;
    @Autowired
    UnderstandingLevelQuestionRepository understandingLevelQuestionRepository;
    AppUser testAppUser;
    UnderstandingLevelLog understandingLevelLog;
    RegistrationRequest request;
    String accessToken;

    @BeforeEach
    void beforeEach() {
        understandingLevelLog = new UnderstandingLevelLog();
        understandingLevelLog.setDate(LocalDateTime.now());
        understandingLevelLog.setPercentageOfUnderstanding(35);

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
        understandingLevelLogRepostitory.findAll().forEach(l -> l.setAppUser(null));
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
        understandingLevelLogRepostitory.deleteAll();
        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
        assertTrue(understandingLevelLogRepostitory.findAll().isEmpty());
    }

    @Test
    void saveLog() throws Exception{
        understandingLevelLog.setPercentageOfUnderstanding(35);
        mockMvc.perform(post("/api/v1/understanding-meter/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(understandingLevelLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.percentageOfUnderstanding", is(35)));
    }

    @Test
    void getLog() throws Exception {
        understandingLevelLog.setPercentageOfUnderstanding(35);
        mockMvc.perform(post("/api/v1/understanding-meter/save/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(understandingLevelLog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.percentageOfUnderstanding", is(35)));

        mockMvc.perform(get("/api/v1/understanding-meter/get/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.percentageOfUnderstanding", is(35)));
    }

    @Test
    void getExercise() throws Exception {
        Resource fileResource = new ClassPathResource("understandingleveltext.txt");
        assertNotNull(fileResource);
        byte[] bytes = fileResource.getInputStream().readAllBytes();
        String text = new String(bytes, StandardCharsets.UTF_8);

        mockMvc.perform(get("/api/v1/understanding-meter/text/" + 1)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(String.class)))
                .andExpect(jsonPath("$", is(text)));
    }

    @Test
    void getQuestionsToTextNumberX() throws Exception {
        List<UnderstandingLevelQuestion> list = understandingLevelQuestionRepository.getQuestionsToTextNumberX(1L);

        mockMvc.perform(get("/api/v1/understanding-meter/questions/" + 1)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].question", is(list.get(0).getQuestion())));
    }
}