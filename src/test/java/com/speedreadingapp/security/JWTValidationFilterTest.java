package com.speedreadingapp.security;

import com.auth0.jwt.JWT;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JWTValidationFilterTest {

    private final String TEST_ENDPOINT = "/test";

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockApplicationUserFactory mockApplicationUserFactory;

    @Autowired
    JWTAlgorithmProvider jwtAlgorithmProvider;


    @Test
    void attemptAuthorizeSuccess() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 second
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", applicationUser.getRoles().stream().map(Role::toString).toList())
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void attemptAuthorizeWithWrongTokenFailed() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 second
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", applicationUser.getRoles().stream().map(Role::toString).toList())
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        StringBuilder stringBuilder = new StringBuilder(accessToken);
        accessToken = stringBuilder.delete(50, 50).insert(55, 'X').toString();
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void attemptAuthorizeFailedWithClaimsFromTokenNotCoverWithClaimsFromDatabase() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        List<String> listClaims = new ArrayList<>(applicationUser.getRoles().stream().map(Role::toString).toList());
        listClaims.add("ADMIN");

        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 second
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", listClaims)
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void attemptAuthorizeFailedWhenUserIsNotInDatabaseButTokenIsCorrect() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000L)) // 10 second
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", applicationUser.getRoles().stream().map(Role::toString).toList())
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());


        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.empty());

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers
                                .jsonPath("$.errors[0].errorMessage")
                                .value(String.format("User with email %s not found", applicationUser.getEmail()))
                );
    }


    @Test
    void attemptAuthorizeFailedWithExpiredToken() throws Exception {
        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();
        String accessToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() - 10000L)) // Create token with 10s expired period
                .withSubject(applicationUser.getEmail())
                .withClaim("username", applicationUser.getEmail())
                .withClaim("roles", applicationUser.getRoles().stream().map(Role::toString).toList())
                .withClaim("generation-datetime", Instant.now())
                .sign(jwtAlgorithmProvider.getAlgorithm());

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail()))
                .thenReturn(Optional.of(applicationUser));

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(TEST_ENDPOINT)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


}