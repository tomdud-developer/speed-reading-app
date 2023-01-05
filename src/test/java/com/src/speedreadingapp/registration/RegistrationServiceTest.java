package com.src.speedreadingapp.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.jpa.appuser.*;
import com.src.speedreadingapp.jpa.course.UserProgress;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.jpa.course.UserProgressService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ExtendWith(MockitoExtension.class)
@Transactional
class RegistrationServiceTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
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
        testAppUser.setUsername("testUsername");
        testAppUser.setEmail("test@test.gmail.com");
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
        userProgressRepository.findAll().forEach(userProgress -> userProgress.setAppUser(null));
        appUserService.getUsers().forEach(user -> user.setUserProgress(null));
        confirmationTokenRepository.deleteAll();
        appUserService.getUsers().forEach(user -> user.getRoles().clear());
        userProgressRepository.deleteAll();
        appUserRepository.deleteAll();
        roleRepository.deleteAll();


        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
    }

    @Test
    void registerNotValidEmail() {
        //Create not valid email
        request.setEmail("test@.com");
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.register(request);
        });

        String expectedMessage = "Email not valid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void registerValidEmail() {
        registrationService.register(request);
        AppUser appUser = appUserRepository.findByUsername(request.getUsername());

        assertEquals(request.getUsername(), appUser.getUsername());
    }
    @Test
    void confirmTokenProperly() {
        String token = registrationService.register(request);
        String isConfimred = registrationService.confirmToken(token);
        assertEquals("confirmed", isConfimred, "confirmed");

        AppUser appUser = appUserRepository.findByUsername(request.getUsername());

        //check roles of user
        Role userRole = roleRepository.findByName("ROLE_USER");
        List<Role> roles = appUser.getRoles().stream().toList();
        assertTrue(roles.contains(userRole));


        //Has progress entity
        assertNotNull(appUser.getUserProgress());
    }

    @Test
    void confirmTokenIncorrectToken()  {
        String token = registrationService.register(request);
        AppUser appUser = appUserRepository.findByUsername(request.getUsername());

        request.setEmail("test@.com");
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            registrationService.confirmToken(token + "brokeThisToken");
        });

        String expectedMessage = "token not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}