package com.speedreadingapp.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.RefreshTokenResponseDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.repository.ApplicationUserRepository;
import com.speedreadingapp.security.token.JWTAlgorithmProvider;
import com.speedreadingapp.util.MockApplicationUserFactory;
import com.speedreadingapp.util.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TokenControllerTest {

    private final String VERIFY_ENDPOINT = "/api/v2/token/verify";
    private final String REFRESH_ENDPOINT = "/api/v2/token/refresh";

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockApplicationUserFactory mockApplicationUserFactory;

    @Autowired
    JWTAlgorithmProvider jwtAlgorithmProvider;

    @Test
    void verifyAccessTokenSuccess() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 second
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", applicationUser.getRoles().stream().map(Role::toString).toList())
                .withClaim("generation-datetime", Instant.now())
                .withClaim("token-type", "access_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VERIFY_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results")
                        .value("Token is valid"));
    }

    @Test
    void verifyAccessTokenFailed() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000L)) // 10 second expired token
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", applicationUser.getRoles().stream().map(Role::toString).toList())
                .withClaim("generation-datetime", Instant.now())
                .withClaim("token-type", "access_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VERIFY_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void verifyAccessTokenFailedBecauseOfLackOfTokenTypeClaim() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 second
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", applicationUser.getRoles().stream().map(Role::toString).toList())
                .withClaim("generation-datetime", Instant.now())
                //disabled token-type
                //.withClaim("token-type", "access_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VERIFY_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void verifyRefreshTokenSuccess() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 seconds valid
                .withSubject(applicationUser.getEmail())
                .withIssuer("example.com/api/v2/verify")
                .withClaim("token-type", "refresh_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VERIFY_ENDPOINT)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results")
                        .value("Token is valid"));
    }

    @Test
    void verifyRefreshTokenFailed() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000L)) // 10 second expired token
                .withSubject(applicationUser.getEmail())
                .withIssuer("example.com/api/v2/verify")
                .withClaim("token-type", "refresh_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VERIFY_ENDPOINT)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshTokenSuccess() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 seconds valid
                .withSubject(applicationUser.getEmail())
                .withIssuer("example.com/api/v2/verify")
                .withClaim("token-type", "refresh_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(REFRESH_ENDPOINT)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void refreshTokenSuccessAndThenValidateIt() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 seconds valid
                .withSubject(applicationUser.getEmail())
                .withIssuer("example.com/api/v2/verify")
                .withClaim("token-type", "refresh_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(REFRESH_ENDPOINT)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();

        //given2
        ApiResponse<RefreshTokenResponseDTO> apiResponse = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        //then2
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VERIFY_ENDPOINT)
                        .header("Authorization", "Bearer " + apiResponse.getResults().getAccess_token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results")
                        .value("Token is valid"));
    }

    @Test
    void refreshTokenFailedBecauseOfExpired() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000L)) // 10 seconds expired
                .withSubject(applicationUser.getEmail())
                .withIssuer("example.com/api/v2/verify")
                .withClaim("token-type", "refresh_token")
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(REFRESH_ENDPOINT)
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}