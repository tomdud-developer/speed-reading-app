package com.speedreadingapp.security;

import com.speedreadingapp.controller.LoginAndRegisterController;
import com.speedreadingapp.dto.LoginRequestDTO;
import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.repository.ApplicationUserRepository;
import com.speedreadingapp.service.ValueMapper;
import com.speedreadingapp.util.MockApplicationUserFactory;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JWTGeneratorFilterTest {

    private final String LOGIN_ENDPOINT = "/api/v1/login";

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockApplicationUserFactory mockApplicationUserFactory;


    @Test
    void attemptAuthenticationSuccess() throws Exception {
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@test.com");
        loginRequestDTO.setPassword("testPassword");

        Mockito.when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOGIN_ENDPOINT)
                        .content(Objects.requireNonNull(
                                ObjectToJsonAsStringConverter.convert(loginRequestDTO))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").exists());
    }

    @Test
    void attemptAuthenticationFailed() throws Exception {
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@test.com");
        loginRequestDTO.setPassword("testBadPassword");

        Mockito.when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(LOGIN_ENDPOINT)
                        .content(Objects.requireNonNull(
                                ObjectToJsonAsStringConverter.convert(loginRequestDTO))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("$.errors[0].errorMessage")
                                .value("Invalid login or password"));
    }
}