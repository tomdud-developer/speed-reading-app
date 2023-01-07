package com.src.speedreadingapp.jpa.appuser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AppUserTest {

    AppUser testAppUser;
    Role testRole;
    Set<Role> setRoles;

    @BeforeEach
    void setUp() {
        testRole = new Role(1L, "ROLE_USER");
        setRoles = new HashSet<>();
        setRoles.add(testRole);

        testAppUser = new AppUser();
        testAppUser.setUsername("testUsername");
        testAppUser.setEmail("test@test.gmail.com");
        testAppUser.setFirstname("testFirstname");
        testAppUser.setLastname("testLastname");
        testAppUser.setPassword("testPassword");
        testAppUser.setLocked(false);
        testAppUser.setRoles(setRoles);
    }

    @AfterEach
    void tearDown() {

    }
    @Test
    void getAuthorities() {
        List<? extends GrantedAuthority> list = testAppUser.getAuthorities().stream().toList();
        String roleName = list.get(0).getAuthority();
        Assertions.assertEquals(testRole.getName(), roleName);
    }
    @Test
    void isAccountNonLocked() {
        Assertions.assertTrue(testAppUser.isAccountNonLocked());
    }

    @Test
    void getEnabled() {
    }

    @Test
    void getRoles() {
    }

    @Test
    void getSpeedMeterLogs() {
    }

    @Test
    void getPdfUser() {
    }

    @Test
    void getSchultzArrayLog() {
    }

    @Test
    void getColumnNumberExerciseLog() {
    }

    @Test
    void getNumbersDisappearExerciseLog() {
    }

    @Test
    void getUserProgress() {
    }

    @Test
    void getUnderstandingLevelLog() {
    }

    @Test
    void setId() {
    }

    @Test
    void setFirstname() {
    }

    @Test
    void setLastname() {
    }

    @Test
    void setUsername() {
    }

    @Test
    void setEmail() {
    }

    @Test
    void setPassword() {
    }

    @Test
    void setLocked() {
    }

    @Test
    void setEnabled() {
    }

    @Test
    void setRoles() {
    }

    @Test
    void setSpeedMeterLogs() {
    }

    @Test
    void setPdfUser() {
    }

    @Test
    void setSchultzArrayLog() {
    }

    @Test
    void setColumnNumberExerciseLog() {
    }

    @Test
    void setNumbersDisappearExerciseLog() {
    }

    @Test
    void setUserProgress() {
    }

    @Test
    void setUnderstandingLevelLog() {
    }
}