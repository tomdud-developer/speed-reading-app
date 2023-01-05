package com.src.speedreadingapp.jpa.appuser;

import com.src.speedreadingapp.SpeedReadingAppApplication;
import com.src.speedreadingapp.registration.token.ConfirmationToken;
import com.src.speedreadingapp.registration.token.ConfirmationTokenRepository;
import com.src.speedreadingapp.registration.token.ConfirmationTokenService;
import com.src.speedreadingapp.security.config.PasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpeedReadingAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
    @Autowired
    AppUserService appUserService;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;



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
    }

    @AfterEach
    void afterEach() {
        //clear database
        appUserService.getUsers().forEach(user -> user.getRoles().clear());
        confirmationTokenRepository.deleteAll();
        appUserRepository.deleteAll();
        roleRepository.deleteAll();

        //Check isUserRepositoryEmpty
        assertTrue(confirmationTokenRepository.findAll().isEmpty());
        assertTrue(roleRepository.findAll().isEmpty());
        assertTrue(appUserRepository.findAll().isEmpty());
    }
    @Test
    void loadUserByUsername() {
        appUserService.signUpUser(testAppUser);
        UserDetails userDetails = appUserService.loadUserByUsername(testAppUser.getUsername());
        Assertions.assertEquals(testAppUser.getUsername(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameUserNotFound() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.loadUserByUsername(testAppUser.getUsername());
        });

        String expectedMessage = "User" + testAppUser.getUsername() + "not found in the database";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void signUpNewUserCanDoItCorrect() {
        String token = appUserService.signUpUser(testAppUser);
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(testAppUser.getEmail());

        //Check isNotEmpty and have properly email
        assertThat(optionalAppUser).isNotEmpty();
        AppUser supposedAppUser = optionalAppUser.get();
        Assertions.assertEquals(testAppUser.getEmail(), supposedAppUser.getEmail());

        //Check token in database
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByToken(token);
        assertThat(optionalConfirmationToken).isNotEmpty();
        ConfirmationToken supposedToken = optionalConfirmationToken.get();
        Assertions.assertEquals(supposedAppUser.getId(), supposedToken.getAppUser().getId());
    }

    @Test
    void signUpNewUserThrowEmailIsRegistered() {
        appUserService.signUpUser(testAppUser);
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(testAppUser.getEmail());
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            appUserService.signUpUser(testAppUser);
        });

        String expectedMessage = "Email " + testAppUser.getEmail() + " is registered!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void enableAppUser() {
        appUserService.signUpUser(testAppUser);
        appUserService.enableAppUser(testAppUser.getEmail());
        AppUser appUser = appUserService.getAppUser(testAppUser.getUsername());
        boolean isEnabled = appUser.isEnabled();
        assertTrue(isEnabled);
    }

    @Test
    void getAppUser() {
        appUserService.signUpUser(testAppUser);
        AppUser appUser = appUserService.getAppUser(testAppUser.getUsername());
        assertEquals(testAppUser.getUsername(), appUser.getUsername());
    }

    @Test
    void getUsers() {
        ArrayList<AppUser> appUserArrayList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            AppUser appUser = new AppUser();
            appUser.setUsername("testUsername" + i);
            appUser.setEmail("test" + i + "@test.gmail.com");
            appUser.setFirstname("testFirstname" + i);
            appUser.setLastname("testLastname" + i);
            appUser.setPassword("testPassword" + i);
            appUserArrayList.add(appUser);
        }
        appUserArrayList.forEach(appUserService::signUpUser);
        List<AppUser> listFromDatabase = appUserService.getUsers();

        assertEquals(appUserArrayList.size(), listFromDatabase.size());
    }

    @Test
    void saveRole() {
        Role role = new Role(1L, "ROLE_USER");
        Role savedRole = appUserService.saveRole(role);
        Role roleFromDatabase = roleRepository.findByName("ROLE_USER");

        assertEquals(role.getName(), savedRole.getName());
        assertEquals(savedRole, roleFromDatabase);
    }

    @Test
    void addRoleToUser() {
        appUserService.signUpUser(testAppUser);
        Role role = new Role(1L, "ROLE_USER");
        Role savedRole = appUserService.saveRole(role);

        appUserService.addRoleToUser(testAppUser.getUsername(), role.getName());
        AppUser appUser = appUserService.getAppUser(testAppUser.getUsername());
        List<Role> expected = List.of(savedRole);
        List<Role> actual = appUser.getRoles().stream().toList();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void findByUsername() {
    }

    @Test
    void finById() {
    }
}