package com.speedreadingapp.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedreadingapp.dto.AuthResponseDTO;
import com.speedreadingapp.dto.LoginRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.repository.ApplicationUserRepository;
import com.speedreadingapp.util.MockApplicationUserFactory;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class JWTValidationFilterTest {

    private final String TEST_ENDPOINT = "/test";
    private final String LOGIN_ENDPOINT = "/api/v1/login";

    @MockBean
    private ApplicationUserRepository applicationUserRepository;
    @MockBean
    private JWTVerifier verifier;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockApplicationUserFactory mockApplicationUserFactory;

    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    void beforeEach() throws Exception {
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@test.com");
        loginRequestDTO.setPassword("testPassword");

        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(LOGIN_ENDPOINT)
                        .content(Objects.requireNonNull(
                                ObjectToJsonAsStringConverter.convert(loginRequestDTO))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").exists())
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        this.authResponseDTO = new ObjectMapper().readValue(jsonResponse, AuthResponseDTO.class);
    }

    @Test
    void attemptAuthorizeSuccess() throws Exception {
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + authResponseDTO.getAccess_token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    //TODO
    void attemptAuthorizeFailedTokenExpired() throws Exception {
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        doThrow(new JWTVerificationException("The Token has expired on xxx.", null))
                .when(verifier).verify(authResponseDTO.getAccess_token());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + authResponseDTO.getAccess_token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("$.errors[0].errorMessage")
                                .value("The Token has expired on xxx."));
    }


}