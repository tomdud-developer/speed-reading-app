package com.src.speedreadingapp.jpa.numbersdisappearexerciselog;

import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.jpa.appuser.*;
import com.src.speedreadingapp.jpa.course.UserProgressRepository;
import com.src.speedreadingapp.registration.RegistrationService;
import com.src.speedreadingapp.registration.token.ConfirmationTokenRepository;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ExtendWith(MockitoExtension.class)
class NumbersDisappearExerciseLogServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

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
    NumbersDisappearExerciseLogService numbersDisappearExerciseLogService;

    @Autowired
    NumbersDisappearExerciseLogRepository numbersDisappearExerciseLogRepository;

    AppUser testAppUser;
    NumbersDisappearExerciseLog numbersDisappearExerciseLog;

    @BeforeEach
    void setUp() {
        numbersDisappearExerciseLog = new NumbersDisappearExerciseLog();
        numbersDisappearExerciseLog.setArray(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        testAppUser = new AppUser();
        testAppUser.setUsername("testUsername");
        testAppUser.setEmail("test@test.gmail.com");
        testAppUser.setFirstname("testFirstname");
        testAppUser.setLastname("testLastname");
        testAppUser.setPassword("testPassword");
        testAppUser.setRoles(new HashSet<>());


        roleRepository.save(new Role(1L,"USER_ROLE"));
        String token = appUserService.signUpUser(testAppUser);
        registrationService.confirmToken(token);
        testAppUser = appUserService.getAppUser(testAppUser.getUsername());
        assertTrue(testAppUser.isEnabled());
    }

    @BeforeEach
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
        numbersDisappearExerciseLogRepository.deleteAll();
        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
        assertTrue(numbersDisappearExerciseLogRepository.findAll().isEmpty());
    }

    @Test
    void saveThrowsUserNotFoundExeption() {
        long idNotExist = testAppUser.getId() + 2312;
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            numbersDisappearExerciseLogService.save(numbersDisappearExerciseLog, idNotExist);
        });

        String expectedMessage = "User " + idNotExist + "doesn't exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void save() {
        numbersDisappearExerciseLogService.save(numbersDisappearExerciseLog, testAppUser.getId());
        NumbersDisappearExerciseLog supposedEntity = appUserService.getAppUser(testAppUser.getUsername()).getNumbersDisappearExerciseLog();
        assertArrayEquals(supposedEntity.getArray(), numbersDisappearExerciseLog.getArray());
    }

    @Test
    void getThrowsUserNotFoundExeption() {
        long idNotExist = testAppUser.getId() + 2312;
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            numbersDisappearExerciseLogService.get(idNotExist);
        });

        String expectedMessage = "User " + idNotExist + "doesn't exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Transactional
    void get() {
        numbersDisappearExerciseLogService.save(numbersDisappearExerciseLog, testAppUser.getId());
        NumbersDisappearExerciseLog supposedEntity = appUserService.getAppUser(testAppUser.getUsername()).getNumbersDisappearExerciseLog();
        assertArrayEquals(supposedEntity.getArray(), numbersDisappearExerciseLog.getArray());

        long expectedEntityId = supposedEntity.getId();
        NumbersDisappearExerciseLog log = numbersDisappearExerciseLogRepository.getReferenceById(expectedEntityId);

        assertNotNull(log);
        assertEquals(supposedEntity, log);
    }



}