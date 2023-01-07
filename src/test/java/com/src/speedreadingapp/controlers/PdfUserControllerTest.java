package com.src.speedreadingapp.controlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.SpeedReadingAppApplicationTests;
import com.src.speedreadingapp.jpa.appuser.*;
import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLog;
import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLogRepository;
import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLogService;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.jpa.pdfuser.PdfUserRepository;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
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
class PdfUserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AppUserService appUserService;
    @Autowired AppUserRepository appUserRepository;
    @Autowired RegistrationService registrationService;
    @Autowired ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired UserProgressRepository userProgressRepository;
    @Autowired PdfUserRepository pdfUserRepository;
    AppUser testAppUser;
    ColumnNumberExerciseLog columnNumberExerciseLog;
    RegistrationRequest request;
    String accessToken;

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
        });

        userProgressRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
        appUserRepository.deleteAll();
        roleRepository.deleteAll();
        pdfUserRepository.deleteAll();
        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
        assertTrue(pdfUserRepository.findAll().isEmpty());
    }

    @Test
    void uploadPdf() throws Exception {
        Resource fileResource = new ClassPathResource("test.pdf");
        assertNotNull(fileResource);
        MockMultipartFile file
                = new MockMultipartFile(
                "multipartPdf",
                "test.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream()
        );

        mockMvc.perform(multipart("/api/v1/pdfuser/save/" + testAppUser.getId() + "&0&0")
                            .file(file)
                            .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void download() throws Exception {
        Resource fileResource = new ClassPathResource("test.pdf");
        assertNotNull(fileResource);
        MockMultipartFile file
                = new MockMultipartFile(
                "multipartPdf",
                "test.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream()
        );

        String correctContent = "Test  Test  Test  ";
        mockMvc.perform(multipart("/api/v1/pdfuser/save/" + testAppUser.getId() + "&0&0")
                        .file(file)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/pdfuser/get/" + testAppUser.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.content", is(correctContent)))
                .andReturn();
    }

    @Test
    void getText() throws Exception{
        Resource fileResource = new ClassPathResource("loremipsum.pdf");
        assertNotNull(fileResource);
        MockMultipartFile file
                = new MockMultipartFile(
                "multipartPdf",
                "loremipsum.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream()
        );

        String correctContent = "lorem ipsum lorem ipsum lorem ipsum lorem ipsum " +
                " Lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum";
        mockMvc.perform(multipart("/api/v1/pdfuser/save/" + testAppUser.getId() + "&0&20")
                        .file(file)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/v1/pdfuser/get-text/" + testAppUser.getId() + "&20")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(String.class)))
                .andReturn();
        long count = result.getResponse().getContentAsString().split(" ").length;
        assertFalse(count >= 22);
        assertFalse(count  <= 18);
    }
}