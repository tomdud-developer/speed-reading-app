package com.speedreadingapp.controller;

import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.repository.ApplicationUserRepository;
import com.speedreadingapp.service.ApplicationUserService;
import com.speedreadingapp.service.ValueMapper;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginAndRegisterControllerTest {

    private static final String REGISTER_ENDPOINT_URL = "/api/v1/register";

    @InjectMocks
    private LoginAndRegisterController loginAndRegisterController;

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before("register")
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.loginAndRegisterController).build();
    }

    @Test
    void givenUser_thenRegister_shouldStatusCreated() throws Exception {
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO
                .builder()
                .email("test@test.com")
                .password("testPassword")
                .firstname("testFirstname")
                .lastname("testLastname")
                .build();

        ApplicationUser applicationUser = ValueMapper.convertToEntity(registerRequestDTO);

        when(applicationUserRepository.save(any())).thenReturn(applicationUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(REGISTER_ENDPOINT_URL)
                        .content(Objects.requireNonNull(
                                ObjectToJsonAsStringConverter.convert(registerRequestDTO))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.results")
                                .value("User has been registered.")
                );
    }

    @Test
    void givenUser_thenRegister_shouldThrowAlreadyRegisteredException() throws Exception {
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO
                .builder()
                .email("test@test.com")
                .password("testPassword")
                .firstname("testFirstname")
                .lastname("testLastname")
                .build();

        ApplicationUser applicationUser = ValueMapper.convertToEntity(registerRequestDTO);

        when(applicationUserRepository.save(any())).thenReturn(applicationUser);
        when(applicationUserRepository.findByEmail(any())).thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(REGISTER_ENDPOINT_URL)
                        .content(Objects.requireNonNull(
                                ObjectToJsonAsStringConverter.convert(registerRequestDTO))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage")
                                .value("This email is already exist")
                );
    }





}